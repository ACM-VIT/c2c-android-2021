package com.acmvit.c2c2021.viewmodels

import android.Manifest
import android.app.Application
import android.app.DownloadManager
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.Log
import android.util.TimeUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.isConnected
import com.acmvit.c2c2021.model.Timings
import com.acmvit.c2c2021.model.Track
import com.acmvit.c2c2021.repository.ResourcesRepository
import com.acmvit.c2c2021.ui.tracks.DownloadBR
import com.acmvit.c2c2021.util.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import org.koin.android.ext.android.get
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class TracksViewModel(
    private val app: Application
) : AndroidViewModel(app) {
    private val TAG = "TracksViewModel"
    private val resourcesRepository: ResourcesRepository = app.get()
    private val disposable = CompositeDisposable()
    private val _eventStartCountDown = CountDown(0)
    private var timeDiff: Long = 0
    private val _submissionEndCountDown = CountDown(0)
    private var clockVerified: Boolean = false
    private val downloadBR = DownloadBR()
    var shouldAnimateCountDown = false
        private set
        get() = if (field) { shouldAnimateCountDown = false; true } else false

    val hasEventStarted = MutableLiveData(resourcesRepository.getCachedEventStartState())
    val hasSubmissionStarted = MutableLiveData(false)
    val timings: MutableLiveData<Timings> = MutableLiveData()
    val tracks: MutableLiveData<List<Track>> = MutableLiveData(listOf())
    val eventStartCountDown: MutableLiveData<List<String>> =
        MutableLiveData(listOf("00", "00", "00"))
    val submissionCountDownText: MutableLiveData<String> = MutableLiveData()
    val selectedTrack: MutableLiveData<Track> = MutableLiveData(null)
    val mainViewEffect: SingleLiveEvent<ViewEffect> = SingleLiveEvent()
    val dialogViewEffect: SingleLiveEvent<ViewEffect> = SingleLiveEvent()
    val submissionCountDownColor: MutableLiveData<Int> = MutableLiveData()
    val downloadingTracks: MutableLiveData<Map<Long, String>> = MutableLiveData(mapOf())

    init {
        app.registerReceiver(downloadBR, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        if (hasEventStarted.value == true) retrieveTracks()

        disposable.addAll(
            downloadBR.downloadCompleteObservable.subscribe {
                val name = downloadingTracks.value?.get(it)
                setAptViewEffect {
                    ViewEffect.ShowSnackbar(
                        app.getString(R.string.downloaded, name),
                        app.getString(R.string.open)
                    ) {
                        openOrDownloadFile("${name}.pdf") {}
                    }
                }
                manageDownloads { remove(it) }
            },

            _eventStartCountDown.asLongObservable().subscribe({ startCd ->
                eventStartCountDown.value = startCd.map { String.format("%02d", it) }
                checkEventStart()
            }, GenericErrorHandler()),

            _submissionEndCountDown.asLongObservable().subscribe({ it ->
                this.submissionCountDownText.value = if (it.sum().toInt() == 0) {
                    app.getString(R.string.submission_completed)
                } else {
                    app.getString(R.string.submision_countdown)
                        .format(it.map { String.format("%02d", it) }.toTypedArray())
                }

                submissionCountDownColor.value = _submissionEndCountDown
                    .getCorrespondingColor(app.COUNTDOWN_COLOR_MAP)

                checkSubmissionsStart()
            }, GenericErrorHandler()),

            resourcesRepository.getTimings().subscribe({ timingsRes ->
                if (timingsRes.data != null) {
                    val times = timingsRes.data
                    timings.value = times
                    checkEventStart()
                    checkSubmissionsStart()

                    times.eventStart?.minus(getCurrentTime())?.let { it1 ->
                        _eventStartCountDown.reset(it1)
                    }

                    times.submissionEnd?.minus(getCurrentTime())?.let { it1 ->
                        _submissionEndCountDown.reset(it1)
                    }
                }
            }, GenericErrorHandler())
        )
    }

    fun selectTrack(pos: Int) {
        if (pos == -1) {
            selectedTrack.value = null
            return
        }
        if (selectedTrack.value != null) return
        selectedTrack.value = tracks.value?.get(pos)
    }

    fun downloadTrack(track: Track) {
        openOrDownloadFile("${track.name}.pdf") {
            if (checkDownloadPermissions { downloadTrack(track) }
                && track.download != null && track.name != null) {

                manageDownloads {
                    downloadFile(track.download, track.name, app,
                        GenericErrorHandler())?.let { put(it, track.name) }
                }

                setAptViewEffect {
                    ViewEffect.ShowSnackbar(
                        app.getString(R.string.downloading, "${track.name}.pdf")
                    )
                }
            }
        }
    }

    fun downloadAllTracks() {
        val tracks = tracks.value
        if (checkDownloadPermissions { downloadAllTracks() } && tracks != null) {
            mainViewEffect.setValue(ViewEffect.ShowConfirmationDialog(
                app.getString(R.string.download_all_tracks_conf_title),
                app.getString(R.string.dowload_all_tracks_conf_msg)
            ) { isConfirmed ->
                if (!isConfirmed) return@ShowConfirmationDialog

                for (track: Track in tracks) {
                    if (track.download != null && track.name != null) {
                        manageDownloads {
                            downloadFile(track.download, track.name, app,
                                GenericErrorHandler())?.let { put(it, track.name) }
                        }
                    }
                }

                setAptViewEffect {
                    ViewEffect.ShowSnackbar(
                        app.getString(R.string.downloading, "All")
                    )
                }
            })
        }
    }

    fun invalidateClock() {
        clockVerified = false
    }

    fun isCurrentTrackDownloadingObservable() =
        Transformations.map(downloadingTracks) { it.containsValue(selectedTrack.value?.name) }

    fun verifyClock() {
        resourcesRepository.getCurrentTimeDiff().subscribe({ res ->
            res.data?.let {
                timeDiff = it
                clockVerified = true
            }
        }, GenericErrorHandler())
    }

    private inline fun setAptViewEffect(todo: () -> ViewEffect) {
        (if (selectedTrack.value == null) mainViewEffect else dialogViewEffect).setValue(todo())
    }

    private inline fun manageDownloads(block: MutableMap<Long, String>.() -> Unit) {
        downloadingTracks.value = downloadingTracks.value?.toMutableMap()?.apply(block)
    }

    private fun getCurrentTime() = Date().time + timeDiff
    private fun checkDownloadPermissions(todo: () -> Unit): Boolean {
        val permitted =
            app.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if (!permitted) {
            setAptViewEffect {
                ViewEffect.RequestPermission(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ) {
                    if (it) todo()
                }
            }
        }

        return permitted
    }

    private fun checkEventStart() {
        clockVerifiedRun({
            if (timings.value?.eventStart!! <= getCurrentTime() && hasEventStarted.value == false) {
                shouldAnimateCountDown = true
                hasEventStarted.value = true
                resourcesRepository.cacheEventStartState(true)
                retrieveTracks()
            }

            if (timings.value?.eventStart!! > getCurrentTime() && hasEventStarted.value == true) {
                hasEventStarted.value = false
                resourcesRepository.cacheEventStartState(false)
            }
        }, { checkEventStart() })
    }

    private fun retrieveTracks() {
        disposable.add(resourcesRepository.getTracks().subscribe({
            if (it.status == Status.SUCCESS) {
                tracks.value = it.data
            }
        }, GenericErrorHandler()))
    }

    private fun checkSubmissionsStart() {
        clockVerifiedRun({
            if (timings.value?.submissionStart!! <= getCurrentTime()) {
                if (hasSubmissionStarted.value == false) hasSubmissionStarted.value = true
            } else {
                if (hasSubmissionStarted.value == true) hasSubmissionStarted.value = false
            }
        }, { checkSubmissionsStart() })
    }

    private inline fun clockVerifiedRun(todo: () -> Unit, crossinline doAfterVerify: () -> Unit) {
        if (!clockVerified) {
            disposable.add(resourcesRepository.getCurrentTimeDiff().subscribe({ res ->
                res.data?.let {
                    timeDiff = it
                    doAfterVerify()
                }
            }, GenericErrorHandler()))
        } else {
            todo()
        }
    }

    private inline fun openOrDownloadFile(filename: String, todo: () -> Unit) {
        val file = checkFileExists(filename)
        if (file != null) {
            setAptViewEffect { ViewEffect.OpenPdf(file) }
            return
        } else {
            todo()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
        disposable.dispose()
        _submissionEndCountDown.stop()
        _eventStartCountDown.stop()
        app.unregisterReceiver(downloadBR)
    }

    sealed class ViewEffect {
        data class RequestPermission(
            val perms: Array<String>,
            val todo: (isSuccessful: Boolean) -> Unit
        ) : ViewEffect()

        data class OpenPdf(val file: File) : ViewEffect()
        data class ShowSnackbar(
            val msg: String,
            val actionName: String = "",
            val action: () -> Unit = { },
        ) : ViewEffect()

        data class ShowConfirmationDialog(
            val title: String,
            val msg: String,
            val todo: (isSuccessful: Boolean) -> Unit
        ) : ViewEffect()
    }

    inner class GenericErrorHandler : Consumer<Throwable> {
        override fun accept(t: Throwable?) {
            setAptViewEffect {
                ViewEffect.ShowSnackbar(
                    when (t) {
                        is NetworkException -> app.getString(R.string.network_err_msg)
                        else -> {
                            Log.d(TAG, "accept: $t")
                            app.getString(R.string.randomErr)
                        }
                    }
                )
            }
        }
    }

}

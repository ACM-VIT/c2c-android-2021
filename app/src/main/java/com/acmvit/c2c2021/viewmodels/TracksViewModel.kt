package com.acmvit.c2c2021.viewmodels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.model.Timings
import com.acmvit.c2c2021.model.Track
import com.acmvit.c2c2021.repository.ResourcesRepository
import com.acmvit.c2c2021.util.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import org.koin.android.ext.android.get
import java.util.*

class TracksViewModel(
    private val app: Application
) : AndroidViewModel(app) {
    private val TAG = "TracksViewModel"
    private val resourcesRepository: ResourcesRepository = app.get()
    private val disposable = CompositeDisposable()
    private val _eventStartCountDown = CountDown(0)
    private var timeDiff: Long = 0
    private val _submissionEndCountDown = CountDown(0)

    val hasEventStarted = MutableLiveData(false)
    val hasSubmissionStarted = MutableLiveData(false)
    val timings: MutableLiveData<Timings> = MutableLiveData()
    val tracks: MutableLiveData<List<Track>> = MutableLiveData()
    val eventStartCountDown: MutableLiveData<List<String>> = MutableLiveData(listOf("00", "00", "00"))
    val submissionEndCountDown: MutableLiveData<List<String>> = MutableLiveData()
    val selectedTrack: MutableLiveData<Track> = MutableLiveData(null)
    val viewEffect: SingleLiveEvent<ViewEffect> = SingleLiveEvent()
    val submissionCountDownColor: MutableLiveData<Int> = MutableLiveData()

    init {
        disposable.addAll(
            resourcesRepository.getCurrentTimeDiff().subscribe ({
                if (it.data != null) {
                    timeDiff = it.data
                }
            }, GenericErrorHandler()),

            _eventStartCountDown.asLongObservable().subscribe ({ startCd ->
                eventStartCountDown.value = startCd.map { String.format("%02d", it) }
                checkEventStart()
            }, GenericErrorHandler()),

            _submissionEndCountDown.asLongObservable().subscribe ({ it ->
                submissionEndCountDown.value = it.map { String.format("%02d", it) }
                Log.d(TAG, ": ${submissionEndCountDown.value}")
                submissionCountDownColor.value = _submissionEndCountDown
                    .getCorrespondingColor(app.COUNTDOWN_COLOR_MAP)

                checkSubmissionsStart()
            }, GenericErrorHandler()),

            resourcesRepository.getTimings().subscribe ({ timingsRes ->
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
        selectedTrack.value = if (pos == -1) null else tracks.value?.get(pos)
    }

    fun downloadTrack(track: Track) {
        if (checkDownloadPermissions { downloadTrack(track) } && track.download != null && track.name != null) {
            downloadFile(track.download, track.name, app)
            viewEffect.setValue(
                ViewEffect.ShowSnackbar(
                    app.getString(R.string.downloading, "${track.name}.pdf")
                )
            )
        }
    }

    fun downloadAllTracks() {
        val tracks = tracks.value
        if (checkDownloadPermissions { downloadAllTracks() } && tracks != null) {
            for (track: Track in tracks) {
                if (track.download != null && track.name != null) {
                    downloadFile(track.download, track.name, app)
                }
            }

            viewEffect.setValue(
                ViewEffect.ShowSnackbar(
                    app.getString(R.string.downloading, "All")
                )
            )
        }
    }

    private fun getCurrentTime() = Date().time + timeDiff
    private fun checkDownloadPermissions(todo: () -> Unit): Boolean {
        val permitted =
            app.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if (!permitted) {
            viewEffect.setValue(ViewEffect.RequestPermission(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                if (it) todo()
            })
        }

        return permitted
    }

    private fun checkEventStart() {
        if (timings.value?.eventStart!! <= getCurrentTime() && hasEventStarted.value == false) {
            hasEventStarted.value = true
            disposable.add(resourcesRepository.getTracks().subscribe({
                if (it.status == Status.SUCCESS) {
                    tracks.value = it.data
                }
            }, GenericErrorHandler()))
        }

        if (timings.value?.eventStart!! > getCurrentTime() && hasEventStarted.value == true) {
            hasEventStarted.value = false
        }
    }

    private fun checkSubmissionsStart() {
        if (timings.value?.submissionStart!! >= getCurrentTime()) {
            if (hasSubmissionStarted.value == false) hasSubmissionStarted.value = true
        } else {
            if (hasSubmissionStarted.value == true) hasSubmissionStarted.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
        _submissionEndCountDown.stop()
        _eventStartCountDown.stop()
    }

    sealed class ViewEffect {
        data class RequestPermission(
            val perms: Array<String>,
            val todo: (isSuccessful: Boolean) -> Unit
        ) : ViewEffect()

        data class OpenPdf(val uri: Uri) : ViewEffect()
        data class ShowSnackbar(val msg: String) : ViewEffect()
    }

    inner class GenericErrorHandler : Consumer<Throwable> {
        override fun accept(t: Throwable?) {
            viewEffect.setValue(
                ViewEffect.ShowSnackbar(
                    when (t) {
                        is NetworkException -> app.getString(R.string.network_err_msg)
                        else -> {
                            Log.d(TAG, "accept: $t")
                            app.getString(R.string.randomErr)
                        }
                    }
                )
            )
        }
    }
    
}

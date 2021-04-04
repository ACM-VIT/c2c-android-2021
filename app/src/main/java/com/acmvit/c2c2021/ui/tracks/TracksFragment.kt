package com.acmvit.c2c2021.ui.tracks

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.AutoTransition
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.transition.addListener
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.binding.setVisibility
import com.acmvit.c2c2021.binding.setVisibilityGone
import com.acmvit.c2c2021.databinding.FragmentTracksBinding
import com.acmvit.c2c2021.ui.PrizesActivity
import com.acmvit.c2c2021.util.*
import com.acmvit.c2c2021.viewmodels.TracksViewModel
import kotlinx.android.synthetic.main.fragment_tracks.*
import org.koin.android.ext.android.bind
import java.time.Duration
import java.util.*

class TracksFragment : Fragment() {
    private lateinit var storagePermRequester: ActivityResultLauncher<Array<String>>
    private val TAG = "TracksFragment"
    private lateinit var tracksRvAdapter: TracksAdapter
    private lateinit var binding: FragmentTracksBinding
    private val viewModel by navGraphViewModels<TracksViewModel>(R.id.nav_tracks)
    private val navController by lazy { findNavController() }
    private var permissionRequest: TracksViewModel.ViewEffect.RequestPermission? = null
    private var lastSnackBarMsg = ""
    private var lastSnackBarExec = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storagePermRequester =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
                val res = results.values.sumBy { if (it) 1 else 0 }
                permissionRequest?.let { it.todo(res == 1) }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.verifyClock()
    }

    override fun onPause() {
        super.onPause()
        viewModel.invalidateClock()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        binding.prizeBtn.setOnClickListener {
            startActivity(Intent(context, PrizesActivity::class.java))
        }

        initRv(binding.tracksRv)
        initObservers()
    }

    private fun initRv(rv: RecyclerView) {
        tracksRvAdapter = TracksAdapter(listOf()) {
            delayedExecute(300) { viewModel.selectTrack(it) }
        }

        rv.addItemDecoration(
            GridSpacingItemDecoration(
                (rv.layoutManager as GridLayoutManager).spanCount,
                30, true
            )
        )

        rv.adapter = tracksRvAdapter
    }

    private fun runWithAnimation(
        duration: Long = 500,
        transition: Transition = AutoTransition(),
        after: () -> Unit = {},
        block: () -> Unit
    ) {

        transition.addListener({
            TransitionManager.endTransitions(binding.rootLayout)
            after()
        })

        TransitionManager.beginDelayedTransition(binding.rootLayout, transition.apply {
            interpolator = AccelerateDecelerateInterpolator()
            this.duration = duration
        })

        block()
    }

    private fun initObservers() {
        viewModel.hasSubmissionStarted.observe(viewLifecycleOwner) {
            delayedExecute(500) {
                runWithAnimation(duration = 300) {
                    setVisibilityGone(binding.postSubmissionStartGroup, !it)
                }
            }
        }

        viewModel.hasEventStarted.observe(viewLifecycleOwner) { hasStarted ->
            if (!hasStarted) {
                setVisibilityGone(binding.preEventStartGroup, false)
                return@observe
            }

            val exec: () -> Unit = {
                runWithAnimation{
                    setVisibilityGone(binding.preEventStartGroup, true)
                }
                binding.countdownCard.alpha = 1F
            }

            if (viewModel.shouldAnimateCountDown) {
                binding.countdownCard.startAnimation(R.anim.zoom_in_zoom_out_animator, after = {
                    binding.countdownCard.alpha = 0F

                    delayedExecute(700, exec)
                })

            } else exec()
        }

        viewModel.tracks.observe(viewLifecycleOwner) {
            delayedExecute(800) {
                tracksRvAdapter.setTracks(it)
            }
        }
        viewModel.selectedTrack.observe(viewLifecycleOwner) {
            it?.let {
                navController.navigate(TracksFragmentDirections.actionNavTracksToTrackBottomDialog())
            }
        }

        viewModel.mainViewEffect.observe(viewLifecycleOwner) {
            when (it) {
                is TracksViewModel.ViewEffect.ShowSnackbar -> {
                    if (!(lastSnackBarMsg == it.msg
                                && (Date().time - lastSnackBarExec) <= MIN_SNACKBAR_OFFSET)
                    ) {
                        lastSnackBarMsg = it.msg
                        lastSnackBarExec = Date().time
                        showSnackbar(binding.root, it.msg, it.actionName, it.action)
                    }
                }

                is TracksViewModel.ViewEffect.RequestPermission -> {
                    permissionRequest = it
                    storagePermRequester.launch(it.perms)
                }

                is TracksViewModel.ViewEffect.ShowConfirmationDialog -> {
                    it.apply { context?.showAlertDialog(title, msg, todo) }
                }

                is TracksViewModel.ViewEffect.OpenPdf -> {
                    context?.openPdf(it.file)
                }

            }
        }
    }

}
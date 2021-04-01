package com.acmvit.c2c2021.ui.tracks

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.databinding.FragmentTracksBinding
import com.acmvit.c2c2021.util.showSnackbar
import com.acmvit.c2c2021.viewmodels.TracksViewModel
import kotlin.properties.Delegates

class TracksFragment : Fragment() {
    private lateinit var storagePermRequester: ActivityResultLauncher<Array<String>>
    private var orientation by Delegates.notNull<Int>()
    private val TAG = "TracksFragment"
    private lateinit var tracksRvAdapter: TracksAdapter
    private lateinit var binding: FragmentTracksBinding
    private val viewModel by navGraphViewModels<TracksViewModel>(R.id.nav_tracks)
    private val navController by lazy { findNavController() }
    private var permissionRequest: TracksViewModel.ViewEffect.RequestPermission? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orientation = resources.configuration.orientation
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        init()
        initRv(binding.tracksRv)
        initObservers()
    }

    private fun init() {
        Log.d(TAG, "init: ")
        binding.countdownCard.orientation =
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LinearLayout.HORIZONTAL
            } else {
                LinearLayout.VERTICAL
            }
    }

    private fun initRv(rv: RecyclerView) {
        val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

        rv.layoutManager = GridLayoutManager(
            context,
            spanCount, GridLayoutManager.VERTICAL, false
        )

        tracksRvAdapter = TracksAdapter(listOf()) {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    viewModel.selectTrack(it)
                },
                500
            )
        }
        rv.adapter = tracksRvAdapter
    }

    private fun initObservers() {
        viewModel.tracks.observe(viewLifecycleOwner) { tracksRvAdapter.setTracks(it) }
        viewModel.selectedTrack.observe(viewLifecycleOwner) {
            it?.let {
                navController.navigate(TracksFragmentDirections.actionNavTracksToTrackBottomDialog())
            }
        }

        viewModel.viewEffect.observe(viewLifecycleOwner) {
            when (it) {
                is TracksViewModel.ViewEffect.ShowSnackbar -> {
                    showSnackbar(binding.root, it.msg)
                }

                is TracksViewModel.ViewEffect.RequestPermission -> {
                    permissionRequest = it
                    storagePermRequester.launch(it.perms)
                }

                is TracksViewModel.ViewEffect.OpenPdf -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = it.uri
                    intent.type = "application/pdf"
                    startActivity(intent)
                }
            }
        }
    }

}
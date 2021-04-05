package com.acmvit.c2c2021.ui.info_sponsers

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.databinding.FragmentInformationBinding
import com.acmvit.c2c2021.model.About
import com.acmvit.c2c2021.ui.OverlayFrame
import com.acmvit.c2c2021.ui.adapters.AboutAdapter
import com.acmvit.c2c2021.ui.adapters.FaqAdapter
import com.acmvit.c2c2021.viewmodels.FaqViewModel
import kotlinx.android.synthetic.main.activity_main.*

class InformationFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var overlayFrame: OverlayFrame
    private var overlayDrawable: ColorDrawable? = null

    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!
    private val aboutList = mutableListOf<About>()
    private val faqAdapter = FaqAdapter()
    private val viewModel by lazy {
        ViewModelProvider(this).get(FaqViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        overlayDrawable =
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.loadingOverlay))
        progressBar = requireActivity().progress_bar_overlay
        overlayFrame = requireActivity().overlay_frame
        overlayFrame.displayOverlay(true, overlayDrawable!!)
        progressBar.visibility = View.VISIBLE

        populateList()
        setUpViewPager()
        binding.leftArrow.setOnClickListener {
            val tab = binding.aboutVp.currentItem
            if (tab != 0)
                binding.aboutVp.currentItem = tab - 1
        }
        binding.rightArrow.setOnClickListener {
            val tab = binding.aboutVp.currentItem
            if (tab != 3)
                binding.aboutVp.currentItem = tab + 1
        }
        fetchFaq()
    }

    private fun fetchFaq() {
        viewModel.faqList.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                faqAdapter.submitList(it)
                binding.rvFaq.adapter = faqAdapter
                overlayFrame.displayOverlay(false, overlayDrawable!!)
                progressBar.visibility = View.INVISIBLE
            }
        })
    }

    private fun setUpViewPager() {
        val aboutAdapter = AboutAdapter(aboutList)
        binding.aboutVp.adapter = aboutAdapter
        binding.aboutVp.setCurrentItem(1, false)
        var curPos = binding.aboutVp.currentItem
        binding.aboutVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                curPos = position
            }

            override fun onPageScrollStateChanged(@ScrollState state: Int) {
                if (curPos == 3)
                    binding.aboutVp.setCurrentItem(1, false)

                if (curPos == 0)
                    binding.aboutVp.setCurrentItem(2, false)
            }
        })
    }

    private fun populateList() {
        aboutList.add(
            About(
                getString(R.string.about_c2c),
                getString(R.string.about_c2c_content),
                getString(R.string.c2c_website_url),
                getString(R.string.c2c_insta_url),
                getString(R.string.c2c_linkedin_url)
            )
        )

        aboutList.add(
            About(
                getString(R.string.about_acm_vit),
                getString(R.string.about_acm_vit_content),
                getString(R.string.acm_website_url),
                getString(R.string.acm_insta_url),
                getString(R.string.acm_linkedin_url)
            )
        )

        aboutList.add(
            About(
                getString(R.string.about_c2c),
                getString(R.string.about_c2c_content),
                getString(R.string.c2c_website_url),
                getString(R.string.c2c_insta_url),
                getString(R.string.c2c_linkedin_url)
            )
        )

        aboutList.add(
            About(
                getString(R.string.about_acm_vit),
                getString(R.string.about_acm_vit_content),
                getString(R.string.acm_website_url),
                getString(R.string.acm_insta_url),
                getString(R.string.acm_linkedin_url)
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        overlayFrame.displayOverlay(false, overlayDrawable!!)
        progressBar.visibility = View.INVISIBLE
    }
}
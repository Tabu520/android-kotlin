package com.taipt.mp3tik.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.taipt.mp3tik.databinding.LayoutRateAppBottomSheetBinding

class RateAppBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: LayoutRateAppBottomSheetBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "RateAppBottomSheetDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutRateAppBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            if (ratingBar.rating > 0) {
                binding.btnRateApp.visibility = View.VISIBLE
            } else {
                binding.btnRateApp.visibility = View.INVISIBLE
            }
        }

        binding.ibCloseBtmSheet.setOnClickListener {
            dismiss()
        }

        binding.btnLater.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
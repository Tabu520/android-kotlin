package com.avenue.baseframework.core.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.avenue.baseframework.core.ui.fragments.BaseFragment
import com.avenue.baseframework.core.ui.fragments.BottomSheet
import com.avenue.baseframework.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment() {

    private val TAG = "HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        BottomSheet(object : BottomSheet.BottomSheetCallback {
            override fun onPositive() {
                Toast.makeText(context, "Positive", Toast.LENGTH_SHORT).show()
            }

            override fun onNegative() {
                Toast.makeText(context, "Negative", Toast.LENGTH_SHORT).show()
            }
        }).show(parentFragmentManager, TAG)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
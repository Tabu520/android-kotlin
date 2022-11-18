package com.taipt.mp3tik.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taipt.mp3tik.R
import com.taipt.mp3tik.databinding.FragmentHelpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpFragment : Fragment(R.layout.fragment_help) {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHelpBinding.inflate(inflater, container,  false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
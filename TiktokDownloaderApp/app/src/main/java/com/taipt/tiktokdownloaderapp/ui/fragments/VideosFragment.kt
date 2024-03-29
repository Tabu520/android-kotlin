package com.taipt.tiktokdownloaderapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taipt.tiktokdownloaderapp.R
import com.taipt.tiktokdownloaderapp.databinding.FragmentDownloadBinding
import com.taipt.tiktokdownloaderapp.databinding.FragmentVideosBinding

class VideosFragment : Fragment(R.layout.fragment_videos) {

    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
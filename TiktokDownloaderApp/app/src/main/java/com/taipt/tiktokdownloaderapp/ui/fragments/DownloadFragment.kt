package com.taipt.tiktokdownloaderapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.taipt.tiktokdownloaderapp.R
import com.taipt.tiktokdownloaderapp.data.model.VideoInPending
import com.taipt.tiktokdownloaderapp.databinding.FragmentDownloadBinding
import com.taipt.tiktokdownloaderapp.ui.viewmodels.MainViewModel
import com.taipt.tiktokdownloaderapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DownloadFragment : Fragment(R.layout.fragment_download) {

    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var videoInPending: VideoInPending
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edtInputUrl.doAfterTextChanged {
            if (it.toString().isNotBlank() && it.toString().contains("tiktok")) {
                binding.btnShowInfo.isEnabled = true
                videoInPending = VideoInPending(UUID.randomUUID().toString(), it.toString())
            }
        }
        binding.btnShowInfo.setOnClickListener {
            binding.cvInformation.visibility = View.VISIBLE
        }
        binding.btnDownloadVideo.setOnClickListener {
            mainViewModel.downloadVideo(videoInPending)
        }
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.videoSaveToFileMutableLiveData.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    dismissLoadingDialog()
                }
                is Resource.Error -> {
                    dismissLoadingDialog()
                }
            }
        }
    }

    private fun showLoadingDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.layout_loading_dialog)
        dialog = builder.create()
        dialog.show()
    }

    private fun dismissLoadingDialog() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.taipt.mp3tik.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taipt.mp3tik.R
import com.taipt.mp3tik.databinding.FragmentLanguageBinding
import com.taipt.mp3tik.settings.LocaleHelper


class LanguageFragment : Fragment(R.layout.fragment_language) {

    interface LanguageFragmentListener {
        fun onSettingLanguageDone(language: String)
    }

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    private var listener: LanguageFragmentListener? = null
    private var localeHelper: LocaleHelper? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        localeHelper = LocaleHelper(requireContext())
        if (context is LanguageFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement LanguageFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (localeHelper?.getLanguage().equals("ko")) {
            binding.ibVi.visibility = View.VISIBLE
            binding.ibEn.visibility = View.INVISIBLE
        } else {
            binding.ibVi.visibility = View.INVISIBLE
            binding.ibEn.visibility = View.VISIBLE
        }
        binding.llVietnamese.setOnClickListener {
            var language = "ko"
            binding.ibVi.visibility = View.VISIBLE
            binding.ibEn.visibility = View.INVISIBLE
            localeHelper?.setNewLocale(requireContext(), language)
            listener?.onSettingLanguageDone(language)
        }

        binding.llEnglish.setOnClickListener {
            var language = "en"
            binding.ibVi.visibility = View.INVISIBLE
            binding.ibEn.visibility = View.VISIBLE
            localeHelper?.setNewLocale(requireContext(), language)
            listener?.onSettingLanguageDone(language)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
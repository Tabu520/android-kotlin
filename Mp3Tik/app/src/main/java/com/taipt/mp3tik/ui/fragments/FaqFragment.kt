package com.taipt.mp3tik.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.taipt.mp3tik.R
import com.taipt.mp3tik.data.model.QuestionAnswer
import com.taipt.mp3tik.databinding.FragmentFaqBinding
import com.taipt.mp3tik.ui.adapters.FaqAdapter

class FaqFragment : Fragment(R.layout.fragment_faq) {

    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!

    private var qnaList = ArrayList<QuestionAnswer>()
    private lateinit var faqAdapter: FaqAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFaqList.layoutManager = LinearLayoutManager(requireContext())
        faqAdapter = FaqAdapter(qnaList)
        binding.rvFaqList.adapter = faqAdapter

        val qna1 = QuestionAnswer(
            question = getString(R.string.how_to_download),
            answer = getString(R.string.how_to_download_content).trimIndent(),
            false
        )
        val qna2 = QuestionAnswer(
            question = getString(R.string.cant_find_video),
            answer = getString(R.string.cant_find_video_content).trimIndent(),
            false
        )
        val qna3 = QuestionAnswer(
            question = getString(R.string.downloading_does_not_work),
            answer = getString(R.string.downloading_does_not_work_content).trimIndent(),
            false
        )
        val qna4 = QuestionAnswer(
            question = getString(R.string.video_has_watermark),
            answer = getString(R.string.video_has_watermark_content).trimIndent(),
            false
        )
        val qna5 = QuestionAnswer(
            question = getString(R.string.cant_play_video),
            answer = getString(R.string.cant_play_video_content).trimIndent(),
            false
        )
        qnaList.add(qna1)
        qnaList.add(qna2)
        qnaList.add(qna3)
        qnaList.add(qna4)
        qnaList.add(qna5)
        faqAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
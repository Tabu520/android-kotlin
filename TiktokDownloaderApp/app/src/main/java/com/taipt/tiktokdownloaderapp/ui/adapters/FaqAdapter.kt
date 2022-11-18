package com.taipt.tiktokdownloaderapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taipt.tiktokdownloaderapp.data.model.QuestionAnswer
import com.taipt.tiktokdownloaderapp.databinding.LayoutFaqItemBinding

class FaqAdapter(
    private var qnaList: List<QuestionAnswer>
): RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutFaqItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutFaqItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(qnaList[position]) {
                binding.tvQuestion.text = this.question
                binding.tvAnswer.text = this.answer
                binding.expandedView.visibility = if (this.isExpanded) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                binding.cardLayout.setOnClickListener {
                    this.isExpanded = !this.isExpanded
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return qnaList.size
    }
}
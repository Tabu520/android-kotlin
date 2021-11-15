package com.example.recyclerviewdynamic.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewdynamic.R
import com.example.recyclerviewdynamic.`interface`.ILoadMore
import com.example.recyclerviewdynamic.model.Item
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.loading_layout.view.*

internal class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var progressionBar = itemView.progress_circular
}

internal class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var tvName = itemView.txt_name
    var tvLength = itemView.txt_length
}

class MyAdapter(recyclerView: RecyclerView, internal var activity: Activity, internal var items: MutableList<Item?>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_ITEM_TYPE = 0
    val VIEW_LOADING_TYPE = 1

    internal var loadMore: ILoadMore? = null
    internal var isLoading: Boolean = false
    internal var visibleThresholds: Int = 5
    internal var lastVisibleItem: Int = 0
    internal var totalItemCount: Int = 0

    init {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThresholds) {
                    if (loadMore != null) {
                        loadMore!!.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_ITEM_TYPE) {
            val view = LayoutInflater.from(activity).inflate(R.layout.item_layout, parent, false)
            return ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(activity).inflate(R.layout.loading_layout, parent, false)
            return LoadingViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = items[position]
            holder.tvName.text = item!!.name
            holder.tvLength.text = item.length.toString()
        } else if (holder is LoadingViewHolder) {
            holder.progressionBar.isIndeterminate = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) VIEW_ITEM_TYPE else VIEW_LOADING_TYPE
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setLoaded() {
        isLoading = false
    }

    fun setLoadMore(loadMore: ILoadMore) {
        this.loadMore = loadMore
    }

}
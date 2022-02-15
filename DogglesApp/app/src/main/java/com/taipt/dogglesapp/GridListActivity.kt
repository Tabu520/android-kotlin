package com.taipt.dogglesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.taipt.dogglesapp.adapters.DogCardAdapter
import com.taipt.dogglesapp.const.Layout
import com.taipt.dogglesapp.databinding.ActivityGridListBinding

class GridListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGridListBinding
    lateinit var gridAdapter: DogCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGridListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupGridRecyclerView()
    }

    private fun setupGridRecyclerView() {
        gridAdapter = DogCardAdapter(Layout.GRID)
        binding.gridRecyclerView.apply {
            adapter = gridAdapter
        }
    }
}
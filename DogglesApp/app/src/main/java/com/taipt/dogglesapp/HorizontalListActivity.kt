package com.taipt.dogglesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.taipt.dogglesapp.adapters.DogCardAdapter
import com.taipt.dogglesapp.const.Layout
import com.taipt.dogglesapp.databinding.ActivityHorizontalListBinding
import com.taipt.dogglesapp.databinding.ActivityVerticalListBinding

class HorizontalListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHorizontalListBinding
    lateinit var horizontalAdapter: DogCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHorizontalListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupHorizontalRecyclerView()
    }

    private fun setupHorizontalRecyclerView() {
        horizontalAdapter = DogCardAdapter(Layout.HORIZONTAL)
        binding.horizontalRecyclerView.apply {
            adapter = horizontalAdapter
        }
    }
}
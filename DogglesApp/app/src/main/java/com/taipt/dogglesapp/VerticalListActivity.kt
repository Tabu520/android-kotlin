package com.taipt.dogglesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.taipt.dogglesapp.adapters.DogCardAdapter
import com.taipt.dogglesapp.const.Layout
import com.taipt.dogglesapp.databinding.ActivityVerticalListBinding

class VerticalListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerticalListBinding
    lateinit var verticalAdapter: DogCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerticalListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupVerticalRecyclerView()
    }

    private fun setupVerticalRecyclerView() {
        verticalAdapter = DogCardAdapter(Layout.VERTICAL)
        binding.verticalRecyclerView.apply {
            adapter = verticalAdapter
        }
    }
}
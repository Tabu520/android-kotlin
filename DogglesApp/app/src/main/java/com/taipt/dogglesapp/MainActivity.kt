package com.taipt.dogglesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.taipt.dogglesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVertical.setOnClickListener {
            launchVerticalList()
        }
        binding.btnHorizontal.setOnClickListener {
            launchHorizontalList()
        }
        binding.btnGrid.setOnClickListener {
            launchGridList()
        }
    }

    private fun launchVerticalList() {
        listIntent = Intent(this, VerticalListActivity::class.java)
        startActivity(listIntent)
    }
    private fun launchHorizontalList() {
        listIntent = Intent(this, HorizontalListActivity::class.java)
        startActivity(listIntent)
    }
    private fun launchGridList() {
        listIntent = Intent(this, GridListActivity::class.java)
        startActivity(listIntent)
    }
}
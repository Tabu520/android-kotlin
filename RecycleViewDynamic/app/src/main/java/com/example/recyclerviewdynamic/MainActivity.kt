package com.example.recyclerviewdynamic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewdynamic.`interface`.ILoadMore
import com.example.recyclerviewdynamic.adapter.MyAdapter
import com.example.recyclerviewdynamic.model.Item
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ILoadMore {

    var items: MutableList<Item?> = ArrayList()
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Random 10 first data
        randomTenData()

        // Init View
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(recycler_view, this, items)
        recycler_view.adapter = adapter
        adapter.setLoadMore(this)
    }

    private fun randomTenData() {
        for (i in 0..9) {
            val name = UUID.randomUUID().toString()
            val item = Item(name, name.length)
            items.add(item)
        }
    }

    override fun onLoadMore() {
        if (items.size < 50) {
            items.add(null)
            adapter.notifyItemInserted(items.size - 1)
            // Run on worker thread
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    items.removeAt(items.size - 1)// Remove null item
                    adapter.notifyItemRemoved(items.size)

                    // Random new data
                    val index = items.size
                    val end = index + 10
                    for (i in index until end) {
                        val name = UUID.randomUUID().toString()
                        val item = Item(name, name.length)
                        items.add(item)
                    }
                    adapter.notifyDataSetChanged()
                    adapter.setLoaded()
                }, 3000)
        } else {
            Toast.makeText(this, "MAX DATA IS 50", Toast.LENGTH_SHORT).show()
        }
    }
}
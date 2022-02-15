package com.taipt.dogglesapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.taipt.dogglesapp.R
import com.taipt.dogglesapp.const.Layout
import com.taipt.dogglesapp.data.DataSource
import com.taipt.dogglesapp.model.Dog

class DogCardAdapter(
    private val layout: Int
) : RecyclerView.Adapter<DogCardAdapter.DogCardViewHolder>() {

    class DogCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dogImage: ImageView = itemView.findViewById(R.id.iv_dog_image)
        val txtDogAge: TextView = itemView.findViewById(R.id.txt_dog_age)
        val txtDogName: TextView = itemView.findViewById(R.id.txt_dog_name)
        val txtDogHobbies: TextView = itemView.findViewById(R.id.txt_dog_hobbies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogCardViewHolder {
        when (layout) {
            Layout.HORIZONTAL, Layout.VERTICAL -> {
                return DogCardViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.vertical_horizontal_list_item, parent, false)
                )
            }
            Layout.GRID -> return DogCardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.grid_list_item, parent, false)
            )
            else -> DogCardViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.vertical_horizontal_list_item, parent, false)
            )
        }
        return DogCardViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.vertical_horizontal_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DogCardViewHolder, position: Int) {
        val dog: Dog = DataSource.dogs[position]
        holder.itemView.apply {
            Glide.with(this).load(dog.imageResourceId).into(holder.dogImage)
        }
        holder.txtDogName.text = dog.name
        holder.txtDogAge.text = "Age: ${dog.age}"
        holder.txtDogHobbies.text = "Hobbies: ${dog.hobbies}"
    }

    override fun getItemCount(): Int {
        return DataSource.dogs.size
    }
}
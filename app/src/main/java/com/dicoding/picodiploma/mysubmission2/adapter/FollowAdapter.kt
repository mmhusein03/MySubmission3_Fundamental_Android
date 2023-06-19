package com.dicoding.picodiploma.mysubmission2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mysubmission2.databinding.ItemRowUserBinding
import com.dicoding.picodiploma.mysubmission2.networking.ItemsItem

class FollowAdapter(private val listFollow: ArrayList<ItemsItem>) :RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (avatar, id, nama) = listFollow[position]
        holder.binding.apply {
            tvItemUserName.text = nama
            tvId.text = id.toString()
            Glide.with(holder.itemView.context)
                .load(avatar)
                .into(imgItemPhoto)
        }
    }

    override fun getItemCount(): Int = listFollow.size

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)
}
package com.dicoding.picodiploma.mysubmission2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mysubmission2.databinding.ItemRowUserBinding
import com.dicoding.picodiploma.mysubmission2.networking.ItemsItem


class UserAdapter(private val listUser: ArrayList<ItemsItem> ) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (avatar, id, nama) = listUser[position]
        holder.binding.apply {
            tvItemUserName.text = nama
            tvId.text = id.toString()
            Glide.with(holder.itemView.context)
                .load(avatar)
                .into(imgItemPhoto)
        }
        holder.itemView.setOnClickListener {onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

}
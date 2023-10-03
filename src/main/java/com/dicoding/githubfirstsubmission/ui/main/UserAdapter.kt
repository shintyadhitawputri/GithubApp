package com.dicoding.githubfirstsubmission.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubfirstsubmission.data.response.ItemsItem
import com.dicoding.githubfirstsubmission.databinding.ItemUsernameBinding

class UsernameAdapter(private var userList: List<ItemsItem>) : RecyclerView.Adapter<UsernameAdapter.ListUserViewHolder>(){

    inner class ListUserViewHolder(private val binding: ItemUsernameBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("PrivateResource")
        fun bind(user: ItemsItem) {
            binding.tvUsername.text = user.login
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .error(com.google.android.material.R.drawable.mtrl_ic_error)
                .into(binding.imgItemPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val binding = ItemUsernameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListUserViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<ItemsItem>) {
        userList =newData
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("username", user.login)
            holder.itemView.context.startActivity(intentDetail)
        }
    }
}


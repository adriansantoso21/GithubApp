package com.example.githubapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubapp.R
import com.example.githubapp.activity.DetailUser
import com.example.githubapp.databinding.ItemAppBinding
import com.example.githubapp.entity.User
import kotlinx.coroutines.CoroutineScope

class FavoriteAdapter(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {
    var listFavUsers = ArrayList<User>()
        set(listFavUsers) {
            if (listFavUsers.size > 0) {
                this.listFavUsers.clear()
            }
            this.listFavUsers.addAll(listFavUsers)
        }

    fun addItem(User: User) {
        this.listFavUsers.add(User)
        notifyItemInserted(this.listFavUsers.size - 1)
    }

    fun updateItem(position: Int, User: User) {
        this.listFavUsers[position] = User
        notifyItemChanged(position, User)
    }

    fun removeItem(position: Int) {
        this.listFavUsers.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavUsers.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listFavUsers[position])
    }

    override fun getItemCount(): Int = this.listFavUsers.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemAppBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.avatar)
                .apply(RequestOptions().override(250, 250))
                .into(binding.imgPhoto)
            binding.txtName.text = user.username
            binding.txtUsername.text = user.username
            binding.itemApp.setOnClickListener {
                onItemClickCallback.onItemClicked(user, adapterPosition)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(selectedUser: User?, position: Int?)
    }
}

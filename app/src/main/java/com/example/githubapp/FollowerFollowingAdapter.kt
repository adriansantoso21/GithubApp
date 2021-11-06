package com.example.githubapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView

class FollowerFollowingAdapter(private val listFollowerFollowing: ArrayList<User>) : RecyclerView.Adapter<FollowerFollowingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_app, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.itemView.context)
            .load(listFollowerFollowing[position].avatar)
            .apply(RequestOptions().override(250, 250))
            .into(viewHolder.imgPhoto)
        viewHolder.txtName.text = listFollowerFollowing[position].fullName
        viewHolder.txtUsername.text = listFollowerFollowing[position].username
    }
    override fun getItemCount(): Int {
        return listFollowerFollowing.size
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName : TextView = view.findViewById(R.id.txt_name)
        val txtUsername : TextView = view.findViewById(R.id.txt_username)
        val imgPhoto : CircleImageView = view.findViewById(R.id.img_photo)
    }
}
package com.example.githubapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    lateinit var detailcontext: Context
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_app, viewGroup, false)
        detailcontext = viewGroup.context
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.itemView.context)
            .load(listUser[position].avatar)
            .apply(RequestOptions().override(250, 250))
            .into(viewHolder.imgPhoto)
        viewHolder.txtName.text = listUser[position].fullName
        viewHolder.txtUsername.text = listUser[position].username

        viewHolder.itemView.setOnClickListener {
            val data = User(
                listUser[position].username,
                listUser[position].fullName,
                listUser[position].avatar,
                listUser[position].company,
                listUser[position].location,
                listUser[position].bio,
                listUser[position].follower,
                listUser[position].following,
                listUser[position].repository
            )
            val intentDetail = Intent(detailcontext, DetailUser::class.java)
            intentDetail.putExtra(DetailUser.EXTRA_USER, data)
            detailcontext.startActivity(intentDetail)
        }
    }
    override fun getItemCount(): Int {
        return listUser.size
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName : TextView = view.findViewById(R.id.txt_name)
        val txtUsername : TextView = view.findViewById(R.id.txt_username)
        val imgPhoto : CircleImageView = view.findViewById(R.id.img_photo)
    }
}

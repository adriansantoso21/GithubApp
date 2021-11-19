package com.example.githubapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.githubapp.R

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnsplash : Button = findViewById(R.id.btn_splash)
        btnsplash.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_splash -> {
                val moveList = Intent(this@MainActivity, ListViewActivity::class.java)
                startActivity(moveList)
            }
        }
    }
}
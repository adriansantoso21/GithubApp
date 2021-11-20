package com.example.githubapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubapp.R
import com.example.githubapp.db.SettingPreferences
import com.example.githubapp.helper.DarkModeViewModel
import com.example.githubapp.helper.DarkModeViewModelFactory

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = SettingPreferences.getInstance(dataStore)
        val viewModel = ViewModelProvider(
            this,
            DarkModeViewModelFactory(pref)
        )[DarkModeViewModel::class.java]

        viewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                    )
                } else {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                    )
                }
            }
        )

        val btnsplash: Button = findViewById(R.id.btn_splash)
        btnsplash.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_splash -> {
                val moveList = Intent(this@MainActivity, ListViewActivity::class.java)
                startActivity(moveList)
                finishAffinity()
            }
        }
    }
}
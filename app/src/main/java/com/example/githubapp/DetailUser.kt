package com.example.githubapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapp.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.hdodenhof.circleimageview.CircleImageView

abstract class DetailUser : AppCompatActivity() {
    private lateinit var sharenama : TextView

    companion object{
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val tvnama : TextView = findViewById(R.id.nama)
        val img_photo : CircleImageView = findViewById(R.id.photo)
        val tvcompany : TextView = findViewById(R.id.company)
        val tvlocation : TextView = findViewById(R.id.location)
        val tvrepository : TextView = findViewById(R.id.repository)
        val tvfollower : TextView = findViewById(R.id.follower)
        val tvfollowing : TextView = findViewById(R.id.following)
        val tvbio : TextView = findViewById(R.id.bio)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        supportActionBar?.title = user.username
        tvnama.text = user.username
        Glide.with(this)
            .load(user.avatar)
            .into(img_photo)
        tvcompany.text = if(user.company == "null") " - " else user.company
        tvlocation.text = if(user.location == "null") " - " else user.location
        tvrepository.text = user.repository
        tvfollower.text = user.follower
        tvfollowing.text = user.following
        tvbio.text = if(user.bio == "null") "No bio" else user.bio

        //Tab Layout Follower and Following
        val sectionsPagerAdapter = SectionsPagerAdapter(this, user.username)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    fun shareFacebook(view: View) {
        sharenama = findViewById(R.id.nama)
        try {
            val fb = Intent(Intent.ACTION_SEND)
            fb.type = "text/plain"
            fb.putExtra(Intent.EXTRA_TEXT, "Kunjungi aplikasi Github App untuk mengunjungi profil dari " + sharenama.text)
            fb.setPackage("com.facebook.katana")
            startActivity(fb)
        } catch (e: Exception) {
            val toast = Toast.makeText(this, "Download" + " app facebook terlebih dahulu", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
    fun shareToTwitter(view: View) {
        sharenama = findViewById(R.id.nama)
        try {
            val twit = Intent(Intent.ACTION_SEND)
            twit.type = "text/plain"
            twit.putExtra(Intent.EXTRA_TEXT, "Kunjungi aplikasi Github App untuk mengunjungi profil dari " + sharenama.text)
            twit.setPackage("advanced.twitter.android")
            startActivity(twit)
        } catch (e: Exception) {
            val toast = Toast.makeText(this, "Download" + " app twitter terlebih dahulu", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
    fun shareToInstagram(view: View) {
        sharenama = findViewById(R.id.nama)
        try {
            val ig = Intent(Intent.ACTION_SEND)
            ig.type = "text/plain"
            ig.putExtra(Intent.EXTRA_TEXT, "Kunjungi aplikasi Github App untuk mengunjungi profil dari " + sharenama.text)
            ig.setPackage("com.instagram.android")
            startActivity(ig)
        } catch (e: Exception) {
            val toast = Toast.makeText(this, "Download" + " app instagram terlebih dahulu", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
    fun shareToWhatsapp(view: View) {
        sharenama = findViewById(R.id.nama)
        try {
            val wa = Intent(Intent.ACTION_SEND)
            wa.type = "text/plain"
            wa.putExtra(Intent.EXTRA_TEXT, "Kunjungi aplikasi Github App untuk mengunjungi profil dari " + sharenama.text)
            wa.setPackage("com.whatsapp")
            startActivity(wa)
        } catch (e: Exception) {
            val toast = Toast.makeText(this, "Download" + " app whatsapp terlebih dahulu", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}
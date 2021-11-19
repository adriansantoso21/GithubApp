package com.example.githubapp.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.adapter.SectionsPagerAdapter
import com.example.githubapp.databinding.ActivityDetailUserBinding
import com.example.githubapp.databinding.ActivityFavoriteUserBinding
import com.example.githubapp.db.DatabaseContract
import com.example.githubapp.db.UserHelper
import com.example.githubapp.entity.User
import com.example.githubapp.helper.MappingHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class DetailUser : AppCompatActivity(), View.OnClickListener {
    private lateinit var sharenama : TextView
    private lateinit var binding : ActivityDetailUserBinding
    private lateinit var userHelper: UserHelper
    private var user: User? = null
    private var position: Int = 0

    private var convert : String = ""
    private var fav : Boolean = false
    private var userUsername : String = ""
    private var userImage : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        user = intent.getParcelableExtra(EXTRA_USER)
//        position = intent.getParcelableExtra(EXTRA_POSITION,0)

        supportActionBar?.title = user?.username
        userUsername = user?.username!!
        userImage = user?.avatar!!

        favButtonImage(binding, user?.username!!)
        binding.nama.text = user?.username
        binding.company.text = if(user?.company == "null") " - " else user?.company
        binding.location.text = if(user?.location == "null") " - " else user?.location
        binding.repository.text = user?.repository
        binding.following.text = user?.following
        binding.bio.text = if(user?.bio == "null") "No bio" else user?.bio
        if(user!!.follower.contains("K")) binding.follower.text = user?.follower
        else {
            if(user?.follower?.toInt()!! > 1000){
                convert = (user?.follower!!.toInt() / 1000).toString()
                convert += "K"
                convert.toString()
                binding.follower.text = convert
            }
            else binding.follower.text = user?.follower
        }

        Glide.with(this)
            .load(user?.avatar)
            .into(binding.photo)

//        Log.d("Message", "Isi user Fav dari list: ${user!!.isFav}")
//        if(user!!.isFav) binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_24)
//        else binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_border_24)

        binding.fabAdd.setOnClickListener(this)

        //Tab Layout Follower and Following
        val sectionsPagerAdapter = SectionsPagerAdapter(this, user?.username!!)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun favButtonImage(binding: ActivityDetailUserBinding, username:String) {
        var favUser : Boolean = isUserAddedToFav(username)
        Log.d("Message", "Isi variabel fav: ${favUser}")

        if (favUser) binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_24)
        else binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }

    private fun isUserAddedToFav(username: String) : Boolean{
        var flag : Boolean = false
        lifecycleScope.launch {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = userHelper.queryById(username)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = deferredNotes.await()
            Log.d("Message", "Isi cursor: ${users}")
            flag = users.size > 0
            Log.d("Message", "Isi flag: ${flag}")
        }
        return flag
    }

    override fun onClick(view: View) {
        if (view.id == R.id.fab_add) {
            //Log.d("Message", "Isi variabel gambar: ${binding.fabAdd.drawable}")
//            binding.fabAdd.drawable.equals(R.drawable.ic_baseline_favorite_24
            var favUser : Boolean = isUserAddedToFav(userUsername)
            if(favUser){
                userHelper.deleteById(userUsername)
                view.findViewById<FloatingActionButton>(R.id.fab_add).setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }else{
                val username = userUsername
                val fullName = binding.nama.text.toString()
                var avatar = userImage
                val company = binding.company.text.toString()
                val location = binding.location.text.toString()
                val bio = binding.bio.text.toString()
                val follower = binding.follower.text.toString()
                val following = binding.following.text.toString()
                val repository = binding.repository.text.toString()

                val values = ContentValues()
                values.put(DatabaseContract.UserColumns.USERNAME, username)
                values.put(DatabaseContract.UserColumns.FULLNAME, fullName)
                values.put(DatabaseContract.UserColumns.AVATAR, avatar)
                values.put(DatabaseContract.UserColumns.COMPANY, company)
                values.put(DatabaseContract.UserColumns.LOCATION, location)
                values.put(DatabaseContract.UserColumns.BIO, bio)
                values.put(DatabaseContract.UserColumns.FOLLOWER, follower)
                values.put(DatabaseContract.UserColumns.FOLLOWING, following)
                values.put(DatabaseContract.UserColumns.REPOSITORY, repository)
                values.put(DatabaseContract.UserColumns.ISFAV, true)

                userHelper.insert(values)
                view.findViewById<FloatingActionButton>(R.id.fab_add).setImageResource(R.drawable.ic_baseline_favorite_24)
            }

        }
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

    companion object{
        const val EXTRA_USER = "extra_user"
        const val EXTRA_POSITION = "extra_position"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following,
        )
    }
}
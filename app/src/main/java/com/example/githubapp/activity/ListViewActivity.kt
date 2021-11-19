package com.example.githubapp.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.adapter.UserAdapter
import com.example.githubapp.databinding.ActivityListViewBinding
import com.example.githubapp.db.UserHelper
import com.example.githubapp.entity.User
import com.example.githubapp.helper.MappingHelper
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ListViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListViewBinding
    private var UserGithubData: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Github Users"

        val adapter = UserAdapter(UserGithubData)
        binding.levelList.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.levelList.setLayoutManager(layoutManager)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.levelList.addItemDecoration(itemDecoration)
        getAllUser()
    }

    private fun getAllUser() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                val listUser = ArrayList<User>()
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("login")
                        userDetail(name)

                    }
                    val adapter = UserAdapter(listUser)
                    binding.levelList.adapter = adapter
                } catch (e: Exception) {
                    Toast.makeText(this@ListViewActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@ListViewActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun userDetail(name: String) {
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$name"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val username = jsonObject.getString("login")
                    val fullName = jsonObject.getString("name")
                    val avatar = jsonObject.getString("avatar_url")
                    val company = jsonObject.getString("company")
                    val location = jsonObject.getString("location")
                    val bio = jsonObject.getString("bio")
                    val follower = jsonObject.getString("followers")
                    val following = jsonObject.getString("following")
                    val repo = jsonObject.getString("public_repos")
                    val favUser: Boolean = isUserAddedToFav(username)

                    UserGithubData.add(
                        User(
                            username,
                            fullName,
                            avatar,
                            company,
                            location,
                            bio,
                            follower,
                            following,
                            repo,
                            favUser
                        )
                    )
                    val adapter = UserAdapter(UserGithubData)
                    binding.levelList.adapter = adapter

                } catch (e: Exception) {
                    Toast.makeText(this@ListViewActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@ListViewActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchUser(query :String) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/search/users?q=$query"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                val listUser = ArrayList<User>()
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val jsonArray = jsonObject.getJSONArray("items")

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("login")
                        userDetail(name)

                    }
                    val adapter = UserAdapter(listUser)
                    binding.levelList.adapter = adapter
                } catch (e: Exception) {
                    Toast.makeText(this@ListViewActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@ListViewActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                if(query.isNotEmpty()){
                    UserGithubData.clear()
                    searchUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

        private fun isUserAddedToFav(username: String) : Boolean{
            var userHelper: UserHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()
            var flag : Boolean = false
            lifecycleScope.launch {
                val deferredNotes = async(Dispatchers.IO) {
                    val cursor = userHelper.queryById(username)
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                val users = deferredNotes.await()
                //Log.d("Message", "Isi users sebelum: ${users}")
                flag = users.size > 0
            }
            return flag
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.darkMode -> {
                val moveDarkMode = Intent(this@ListViewActivity, DarkModeActivity::class.java)
                startActivity(moveDarkMode)
            }
            R.id.favUser -> {
                val moveFavUser = Intent(this@ListViewActivity, FavoriteUserActivity::class.java)
                startActivity(moveFavUser)
            }
        }
    }

    companion object{
        private val TAG = ListViewActivity::class.java.simpleName
    }
}
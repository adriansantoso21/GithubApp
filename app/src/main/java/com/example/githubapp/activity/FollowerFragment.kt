package com.example.githubapp.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.adapter.FollowerFollowingAdapter
import com.example.githubapp.databinding.FragmentFollowerBinding
import com.example.githubapp.entity.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowerFragment(var username: String) : Fragment() {
    private var userFollowerData: ArrayList<User> = ArrayList()
    private lateinit var adapter: FollowerFollowingAdapter
    private lateinit var binding: FragmentFollowerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        adapter = FollowerFollowingAdapter(userFollowerData)
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        binding.levelListFollower.layoutManager = LinearLayoutManager(activity)
        binding.levelListFollower.adapter = FollowerFollowingAdapter(userFollowerData)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userFollowerData.clear()
        getAllFollower(username)
    }

    private fun getAllFollower(username:String) {
        binding.progressBarFollower.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$username/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBarFollower.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.e(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("login")
                        userDetail(name)
                    }
                    binding.levelListFollower.adapter = adapter
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBarFollower.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
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
                binding.progressBarFollower.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.e(TAG, result)
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

                    Log.e(TAG, username.toString())

                    userFollowerData.add(
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
                            false
                        )
                    )
                    val adapter = FollowerFollowingAdapter(userFollowerData)
                    binding.levelListFollower.adapter = adapter

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.progressBarFollower.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private val TAG = FollowerFragment::class.java.simpleName
    }
}
package com.example.githubapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.adapter.FavoriteAdapter
import com.example.githubapp.databinding.ActivityFavoriteUserBinding
import com.example.githubapp.db.UserHelper
import com.example.githubapp.entity.User
import com.example.githubapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: FavoriteAdapter

//    val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.data != null) {
//            // Akan dipanggil jika request codenya ADD
//            when (result.resultCode) {
//                DetailUser.RESULT_ADD -> {
//                    val note = result.data?.getParcelableExtra<User>(DetailUser.EXTRA_NOTE) as User
//                    adapter.addItem(note)
//                    binding.rvNotes.smoothScrollToPosition(adapter.itemCount - 1)
//                    showSnackbarMessage("Satu item berhasil ditambahkan")
//                }
//                DetailUser.RESULT_UPDATE -> {
//                    val note = result.data?.getParcelableExtra<User>(DetailUser.EXTRA_NOTE) as User
//                    val position = result?.data?.getIntExtra(DetailUser.EXTRA_POSITION, 0) as Int
//                    adapter.updateItem(position, note)
//                    binding.rvNotes.smoothScrollToPosition(position)
//                    showSnackbarMessage("Satu item berhasil diubah")
//                }
//                DetailUser.RESULT_DELETE -> {
//                    val position = result?.data?.getIntExtra(DetailUser.EXTRA_POSITION, 0) as Int
//                    adapter.removeItem(position)
//                    showSnackbarMessage("Satu item berhasil dihapus")
//                }
//            }
//        }
//    }

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Favorite User"


        val layoutManager = LinearLayoutManager(this)

        binding.levelList.setLayoutManager(layoutManager)
        binding.levelList.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.levelList.addItemDecoration(itemDecoration)

        adapter = FavoriteAdapter(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(selectedUser: User?, position: Int?) {
                val intent = Intent(this@FavoriteUserActivity, DetailUser::class.java)
                intent.putExtra(DetailUser.EXTRA_USER, selectedUser)
                //intent.putExtra(DetailUser.EXTRA_POSITION, position)
                startActivity(intent)
                //resultLauncher.launch(intent)
            }
        })
        binding.levelList.adapter = adapter

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavUsers = list
            }
        }
    }

    private fun loadNotesAsync() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val users = deferredNotes.await()
            if (users.size > 0) {
                adapter.listFavUsers = users
            } else {
                adapter.listFavUsers = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
            binding.levelList.adapter = adapter
            Log.d("Message1", "Isi ukuran user: ${users.size}")
            for (user in users) Log.d("Message", "Isi user: ${user}")


            userHelper.close()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavUsers)
    }

    /**
     * Tampilkan snackbar
     *
     * @param message inputan message
     */
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.levelList, message, Snackbar.LENGTH_SHORT).show()
    }
}
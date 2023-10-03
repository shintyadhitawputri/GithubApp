package com.dicoding.githubfirstsubmission.ui.favoritefitur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubfirstsubmission.data.response.ItemsItem
import com.dicoding.githubfirstsubmission.database.UserFavDao
import com.dicoding.githubfirstsubmission.database.UserFavRoomDatabase
import com.dicoding.githubfirstsubmission.databinding.ActivityFavoriteUserBinding
import com.dicoding.githubfirstsubmission.ui.main.UsernameAdapter

@Suppress("DEPRECATION")
class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var userDao: UserFavDao
    private lateinit var adapter: UsernameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = UserFavRoomDatabase.getDatabase(this).UserFavDao()

        val recyclerView = binding.rvFav
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UsernameAdapter(emptyList())
        recyclerView.adapter = adapter

        showLoading(true)
        binding.rvFav.visibility = View.GONE
        displayDatabase()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun displayDatabase() {

        userDao.getAllUserFav().observe(this) { users ->
            Handler().postDelayed({
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            adapter.updateData(items)
                showLoading(false)
            }, 500)
            binding.rvFav.visibility = View.VISIBLE
        }
    }



}
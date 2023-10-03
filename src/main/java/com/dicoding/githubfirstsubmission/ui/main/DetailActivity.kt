package com.dicoding.githubfirstsubmission.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubfirstsubmission.R
import com.dicoding.githubfirstsubmission.data.response.DetailUserResponse
import com.dicoding.githubfirstsubmission.database.UserFav
import com.dicoding.githubfirstsubmission.database.UserFavRoomDatabase
import com.dicoding.githubfirstsubmission.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private var isFav: Boolean = false
    private var avatarUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        val username = intent.getStringExtra("username")

        if (username != null) {
            viewModel.getDetails(username)
            userInFavorite(username)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val fragment = FollowFragment()
        val bundle = Bundle()
        bundle.putString(FollowFragment.ARG_USERNAME, username)
        fragment.arguments = bundle


        viewModel.detailList.observe(this) { detailUserList ->
            if (detailUserList.isNotEmpty()) {
                val detailUser = detailUserList[0]
                binding.tvName.text = detailUser.name
                binding.tvUsername.text = detailUser.login
                binding.followers.text = detailUser.followers.toString()
                binding.following.text = detailUser.following.toString()

                avatarUrl = detailUser.avatarUrl

                Glide.with(this)
                    .load(detailUser.avatarUrl)
                    .into(binding.imgAva)

                binding.btnShare.setOnClickListener {
                    shareInfo(detailUser)
                }
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        if (username != null) {
            sectionsPagerAdapter.username = username
        }
        val viewPager: ViewPager2 = binding.viewPager2
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabsData
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        binding.fabData.setOnClickListener {
            isFav = !isFav
            if (username != null) {
                val user = UserFav(username = username, avatarUrl = avatarUrl)
                if (isFav) {
                    binding.fabData.setImageResource(R.drawable.baseline_favorite_24)
                    saveUser(user)
                } else {
                    binding.fabData.setImageResource(R.drawable.baseline_favorite_border_24)
                    removeUser(username)
                }
            }
        }
            binding.fabData.isActivated = isFav

    }




    private fun showLoading(isLoading: Boolean?) {
        binding.progressBar.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    private fun saveUser(user: UserFav){
        val userDao = UserFavRoomDatabase.getDatabase(this).UserFavDao()
        CoroutineScope(Dispatchers.IO).launch {
            userDao.insert(user)
        }
    }

    private fun removeUser(username: String){
        val userDao = UserFavRoomDatabase.getDatabase(this).UserFavDao()
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getFavoriteUserByUsername(username)
            user?.let {
                userDao.delete(it)
            }
        }
    }

    private fun userInFavorite(username: String){
        val userDao = UserFavRoomDatabase.getDatabase(this).UserFavDao()
        CoroutineScope(Dispatchers.IO).launch {
            val favIcon = username.let { userDao.getFavoriteUserByUsername(it) }
            withContext(Dispatchers.Main){
                isFav = if(favIcon!=null){
                    binding.fabData.setImageResource(R.drawable.baseline_favorite_24)
                    true
                }else{
                    binding.fabData.setImageResource(R.drawable.baseline_favorite_border_24)
                    false
                }
            }
        }
    }

    private fun shareInfo(user: DetailUserResponse){
        val shareProfile = Intent().apply{
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check this Profile! "+
                    "\nUrl: ${user.htmlUrl}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareProfile, "Share this Profile!"))
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
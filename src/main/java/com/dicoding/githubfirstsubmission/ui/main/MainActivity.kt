package com.dicoding.githubfirstsubmission.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubfirstsubmission.R
import com.dicoding.githubfirstsubmission.data.response.ItemsItem
import com.dicoding.githubfirstsubmission.databinding.ActivityMainBinding
import com.dicoding.githubfirstsubmission.ui.favoritefitur.FavoriteUserActivity
import com.dicoding.githubfirstsubmission.ui.themefitur.DataPreference
import com.dicoding.githubfirstsubmission.ui.themefitur.SettingPreferences
import com.dicoding.githubfirstsubmission.ui.themefitur.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.userList.observe(this) { userList ->
            setUserData(userList)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvGithub.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvGithub.addItemDecoration(itemDecoration)

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val searchBar = binding.searchBarr
        searchBar.visibility = View.GONE
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.searchBar -> {
                    if (searchBar.visibility == View.VISIBLE) {
                        searchBar.visibility = View.GONE
                        true
                    } else {
                        searchBar.visibility = View.VISIBLE
                        with(binding) {
                            searchView.setupWithSearchBar(searchBar)
                            searchView
                                .editText
                                .setOnEditorActionListener { _, actionId, _ ->
                                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                        val keyword = searchView.text.toString()
                                        if (keyword.isNotEmpty() || keyword.isNotBlank()) {
                                            mainViewModel.getUsers(keyword)
                                        }
                                        searchBar.text = keyword
                                        searchView.hide()
                                        true
                                    } else {
                                        false
                                    }
                                }
                        }
                        true
                    }
                }
                R.id.favUser -> {
                    val favActivity = Intent(this, FavoriteUserActivity::class.java)
                    startActivity(favActivity)
                    true
                }
                R.id.menuPreference -> {
                    val preference = Intent(this, DataPreference::class.java)
                    startActivity(preference)
                    true
                }
                else -> false
            }
        }

    val settingPreferences = SettingPreferences.getInstance(this.dataStore)
    val themeFlow: Flow<Boolean> = settingPreferences.getTheme()


    lifecycleScope.launch {
        themeFlow.collect{isDarkMode ->
            val darkMode = if(isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        AppCompatDelegate.setDefaultNightMode(darkMode)
        }
    }

    }
    private fun setUserData(userList: List<ItemsItem>) {
        val adapter = UsernameAdapter(userList)
        binding.rvGithub.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
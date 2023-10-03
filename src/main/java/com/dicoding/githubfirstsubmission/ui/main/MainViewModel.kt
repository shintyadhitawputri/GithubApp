package com.dicoding.githubfirstsubmission.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubfirstsubmission.data.response.ItemsItem
import com.dicoding.githubfirstsubmission.data.response.UserGitResponse
import com.dicoding.githubfirstsubmission.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _userList = MutableLiveData<List<ItemsItem>>()
    val userList: LiveData<List<ItemsItem>> = _userList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _searchQuery = MutableLiveData<String>()
    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>> = _listFollowers
    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>> = _listFollowing

    companion object {
        const val TAG = "MainViewModel"
    }

    init {
        getUsers("shintya")
    }

    fun getUsers(query: String) {
        _isLoading.value = true
        _searchQuery.value = query
        val client = ApiConfig.getApiService().getGitResponse(query)
        client.enqueue(object : Callback<UserGitResponse> {
            override fun onResponse(
                call: Call<UserGitResponse>,
                response: Response<UserGitResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userList.value = response.body()?.items
                } else {
                    val statusCode = response.code()
                    val errorMessage = response.message()
                    Log.e(TAG, "HTTP Error - Code: $statusCode, Message: $errorMessage")
                }
            }

            override fun onFailure(call: Call<UserGitResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun fetchFollow(username: String) {
        getFollowers(username)
        getFollowing(username)
    }

    private fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowers.value = response.body()
                } else {
                    val statusCode = response.code()
                    val errorMessage = response.message()
                    Log.e(TAG, "HTTP Error - Code: $statusCode, Message: $errorMessage")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                } else {
                    val statusCode = response.code()
                    val errorMessage = response.message()
                    Log.e(TAG, "HTTP Error - Code: $statusCode, Message: $errorMessage")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}

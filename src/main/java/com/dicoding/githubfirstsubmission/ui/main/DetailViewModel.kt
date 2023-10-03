package com.dicoding.githubfirstsubmission.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubfirstsubmission.data.repository.UserFavRepository
import com.dicoding.githubfirstsubmission.data.response.DetailUserResponse
import com.dicoding.githubfirstsubmission.data.retrofit.ApiConfig
import com.dicoding.githubfirstsubmission.database.UserFav
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): AndroidViewModel(application){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _detailList = MutableLiveData<List<DetailUserResponse>>()
    val detailList: LiveData<List<DetailUserResponse>> = _detailList
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username


    fun getDetails(username: String) {
        _isLoading.value = true
        _username.value = username

        val detailUser = ApiConfig.getApiService().getDetailUser(username)
        detailUser.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val getDetailUser = response.body()
                    if (getDetailUser != null) {
                        _detailList.value = listOf(getDetailUser)
                    }

                } else {
                    val statusCode = response.code()
                    val errorMessage = response.message()

                    Log.e(MainViewModel.TAG, "HTTP Error - Code: $statusCode, Message: $errorMessage")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(MainViewModel.TAG, "onFaiilure: ${t.message.toString()}")
            }
        })

    }

    private val mUserFavRepository: UserFavRepository = UserFavRepository(application)
    fun getAllUserFav(): LiveData<List<UserFav>> = mUserFavRepository.getAllUserFav()

}
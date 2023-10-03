package com.dicoding.githubfirstsubmission.ui.favoritefitur

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.githubfirstsubmission.data.repository.UserFavRepository
import com.dicoding.githubfirstsubmission.database.UserFav

class UserFavUpdateViewModel(application: Application) : ViewModel() {
    private val mUserFavRepository: UserFavRepository = UserFavRepository(application)

    fun insert(userFav: UserFav) {
        mUserFavRepository.insert(userFav)
    }
    fun delete(userFav: UserFav) {
        mUserFavRepository.delete(userFav)
    }
}
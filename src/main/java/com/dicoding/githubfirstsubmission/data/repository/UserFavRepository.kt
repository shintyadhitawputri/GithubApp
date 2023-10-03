package com.dicoding.githubfirstsubmission.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubfirstsubmission.database.UserFav
import com.dicoding.githubfirstsubmission.database.UserFavDao
import com.dicoding.githubfirstsubmission.database.UserFavRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserFavRepository(application: Application) {
    private val mUserFavDao: UserFavDao
    private val executorService: ExecutorService =Executors.newSingleThreadExecutor()

    init {
        val db = UserFavRoomDatabase.getDatabase(application)
        mUserFavDao = db.UserFavDao()
    }

    fun getAllUserFav(): LiveData<List<UserFav>> = mUserFavDao.getAllUserFav()

    fun getFavoriteUserByUsername(username: String): UserFav? {
        return mUserFavDao.getFavoriteUserByUsername(username)
    }
    fun insert(userFav: UserFav) {
        executorService.execute { mUserFavDao.insert(userFav) }
    }

    fun delete(userFav: UserFav) {
        executorService.execute { mUserFavDao.delete(userFav) }
    }


}
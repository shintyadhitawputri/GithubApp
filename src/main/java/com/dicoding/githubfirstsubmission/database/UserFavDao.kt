package com.dicoding.githubfirstsubmission.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserFavDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userFav: UserFav)

    @Delete
    fun delete(userFav: UserFav)

    @Query("SELECT * from UserFav ORDER BY username ASC")
    fun getAllUserFav(): LiveData<List<UserFav>>

    @Query("SELECT * FROM UserFav WHERE username = :username LIMIT 1")
    fun getFavoriteUserByUsername(username: String): UserFav?

}

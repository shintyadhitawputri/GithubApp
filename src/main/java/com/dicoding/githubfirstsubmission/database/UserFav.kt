package com.dicoding.githubfirstsubmission.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserFav (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username: String = "",
    @ColumnInfo(name = "profilePic")
    var avatarUrl: String? = null,
    ) : Parcelable
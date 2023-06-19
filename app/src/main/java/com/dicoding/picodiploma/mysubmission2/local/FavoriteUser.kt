package com.dicoding.picodiploma.mysubmission2.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_favorite")
data class FavoriteUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "login")
    val name: String,

    @ColumnInfo(name = "avatar_url")
    val avatar: String
): Serializable
package com.dicoding.picodiploma.mysubmission2.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserFavDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM user_favorite")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("DELETE FROM user_favorite WHERE user_favorite.id = :id")
    suspend fun removeFavUser(id: Int): Int

    @Query("SELECT count(*) FROM user_favorite WHERE user_favorite.id = :id")
    suspend fun checkUser(id: Int): Int
}
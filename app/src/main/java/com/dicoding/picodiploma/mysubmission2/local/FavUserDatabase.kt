package com.dicoding.picodiploma.mysubmission2.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteUser::class], version = 1)
abstract class FavUserDatabase : RoomDatabase() {
    companion object {
        private var instance : FavUserDatabase? = null

        fun getDatabase(context: Context): FavUserDatabase? {
            if (instance == null) {
                synchronized(FavUserDatabase::class) {
                    instance = Room.databaseBuilder(context.applicationContext, FavUserDatabase::class.java, "Fav_user_database").build()
                }
            }
            return instance
        }
    }

    abstract fun userFavDao(): UserFavDao
}
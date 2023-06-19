package com.dicoding.picodiploma.mysubmission2.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.picodiploma.mysubmission2.local.FavUserDatabase
import com.dicoding.picodiploma.mysubmission2.local.FavoriteUser
import com.dicoding.picodiploma.mysubmission2.local.UserFavDao

class FavUserViewModel(application: Application): AndroidViewModel(application) {
    private var userFavDao: UserFavDao?
    private var favUserDb: FavUserDatabase?

    init {
        favUserDb = FavUserDatabase.getDatabase(application)
        userFavDao = favUserDb?.userFavDao()
    }

    fun getFavUser(): LiveData<List<FavoriteUser>>? {
        return userFavDao?.getFavoriteUser()
    }
}
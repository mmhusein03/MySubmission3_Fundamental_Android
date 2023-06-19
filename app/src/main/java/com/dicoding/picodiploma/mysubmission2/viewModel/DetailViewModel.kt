package com.dicoding.picodiploma.mysubmission2.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.mysubmission2.local.FavUserDatabase
import com.dicoding.picodiploma.mysubmission2.local.FavoriteUser
import com.dicoding.picodiploma.mysubmission2.local.UserFavDao
import com.dicoding.picodiploma.mysubmission2.networking.ApiConfig
import com.dicoding.picodiploma.mysubmission2.networking.DetailGitUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _detailUser = MutableLiveData<DetailGitUser>()
    val detailUser : LiveData<DetailGitUser> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private var userFavDao: UserFavDao?
    private var favUserDb: FavUserDatabase?

    init {
        favUserDb = FavUserDatabase.getDatabase(application)
        userFavDao = favUserDb?.userFavDao()
    }

    fun getDetail(name: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(name)
        client.enqueue(object : Callback<DetailGitUser> {
            override fun onResponse(call: Call<DetailGitUser>, response: Response<DetailGitUser>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _detailUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailGitUser>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun addFavorite(username: String, id: Int, avatar: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                id,
                username,
                avatar
            )
            userFavDao?.addFavorite(user)
        }
    }

    suspend fun userCheck(id: Int) = userFavDao?.checkUser(id)

    fun removeUser(id: Int) {
        viewModelScope.launch {
            removeFavorite(id)
        }
    }

    private suspend fun removeFavorite(id: Int) = withContext(Dispatchers.IO) {
        userFavDao?.removeFavUser(id)
    }

    companion object {
        const val TAG = "GIT USER"
    }

}
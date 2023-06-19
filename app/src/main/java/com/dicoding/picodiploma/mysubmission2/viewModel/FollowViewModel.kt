package com.dicoding.picodiploma.mysubmission2.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.mysubmission2.networking.ApiConfig
import com.dicoding.picodiploma.mysubmission2.networking.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _follower = MutableLiveData<ArrayList<ItemsItem>>()
    val follower : LiveData<ArrayList<ItemsItem>> = _follower

    private val _following = MutableLiveData<ArrayList<ItemsItem>>()
    val following : LiveData<ArrayList<ItemsItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val  isLoading : LiveData<Boolean> = _isLoading

    fun getFollow(name : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollow(name, "followers")
        client.enqueue(object : Callback<ArrayList<ItemsItem>> {
            override fun onResponse(
                call: Call<ArrayList<ItemsItem>>,
                response: Response<ArrayList<ItemsItem>>,
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _follower.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                Log.e(TAG1, "onFailure: ${t.message}")
            }

        })
    }

    fun getFollowing(name: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollow(name, "following")
        client.enqueue(object : Callback<ArrayList<ItemsItem>>{
            override fun onResponse(
                call: Call<ArrayList<ItemsItem>>,
                response: Response<ArrayList<ItemsItem>>,
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _following.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                Log.e(TAG2, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        const val TAG1 = "Follower"
        const val TAG2 = "Following"
    }
}
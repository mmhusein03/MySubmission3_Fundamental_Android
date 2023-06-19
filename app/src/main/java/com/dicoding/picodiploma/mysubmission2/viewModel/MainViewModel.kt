package com.dicoding.picodiploma.mysubmission2.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.picodiploma.mysubmission2.networking.ApiConfig
import com.dicoding.picodiploma.mysubmission2.networking.ItemsItem
import com.dicoding.picodiploma.mysubmission2.networking.UserResponse
import com.dicoding.picodiploma.mysubmission2.utils.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    class Factory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(pref) as T
        }
    }

    fun getTheme() = pref.getThemeSetting().asLiveData()

    fun saveTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkMode)
        }
    }

    private val _user = MutableLiveData<List<ItemsItem>>()
    val user: LiveData<List<ItemsItem>> = _user

    private val _userCount = MutableLiveData<Int>()
    val userCount : LiveData<Int> = _userCount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun findUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()?.items
                    _userCount.value = response.body()?.total_count
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
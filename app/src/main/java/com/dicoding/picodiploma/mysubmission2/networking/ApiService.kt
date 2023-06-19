package com.dicoding.picodiploma.mysubmission2.networking

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUser(
        @Query("q") username: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailGitUser>

    @GET("users/{username}/{tipe}")
    fun getFollow(
        @Path("username") username: String,
            @Path("tipe") tipe: String
    ): Call<ArrayList<ItemsItem>>
}
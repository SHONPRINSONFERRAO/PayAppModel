package com.shon.projects.payappmodel.utils.glide.networking

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("posts")
    fun getPhotos(): Call<List<TransactionDataModel>>


    @GET("profile")
    fun getUserData(): Call<ProfileDataModel>

}
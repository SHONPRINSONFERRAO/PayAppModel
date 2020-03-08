package com.shon.projects.payappmodel.utils.glide.networking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileDataModel(

    @Expose
    @SerializedName("user_id")
    val userId: Int,
    @Expose
    @SerializedName("account_balance")
    val balance: Int,
    @Expose
    @SerializedName("name")
    val name: String
)
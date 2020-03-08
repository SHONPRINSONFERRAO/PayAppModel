package com.shon.projects.payappmodel.utils.glide.networking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TransactionDataModel(

    @Expose
    @SerializedName("userId")
    val userId: Integer,
    @Expose
    @SerializedName("id")
    val id: Integer,
    @Expose
    @SerializedName("transaction_type")
    val transactionType: String,
    @Expose
    @SerializedName("time_stamp")
    val timeStamp: Long,
    @Expose
    @SerializedName("user_name")
    val userName: String,
    @Expose
    @SerializedName("amount_transaction")
    val amountTransaction: Integer
)
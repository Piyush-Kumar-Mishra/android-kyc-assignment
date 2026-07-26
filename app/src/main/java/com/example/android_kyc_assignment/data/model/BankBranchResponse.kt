package com.example.android_kyc_assignment.data.model

import com.google.gson.annotations.SerializedName

data class BankBranchResponse(
    @SerializedName("BANK") val bank: String,
    @SerializedName("BRANCH") val branch: String,
    @SerializedName("CITY") val city: String,
    @SerializedName("STATE") val state: String,
    @SerializedName("ADDRESS") val address: String
)

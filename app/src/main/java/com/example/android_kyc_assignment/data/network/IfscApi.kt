package com.example.android_kyc_assignment.data.network

import com.example.android_kyc_assignment.data.model.BankBranchResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface IfscApi {
    @GET("{ifsc}")
    suspend fun getBankDetails(@Path("ifsc") ifscCode: String): BankBranchResponse
}

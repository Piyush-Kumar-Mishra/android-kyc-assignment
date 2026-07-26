package com.example.android_kyc_assignment.data.repository

import com.example.android_kyc_assignment.data.model.BankBranchResponse
import com.example.android_kyc_assignment.data.network.IfscApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IfscRepository @Inject constructor(
    private val ifscApi: IfscApi
) {
    private val cache = HashMap<String, BankBranchResponse>()

    suspend fun getBankDetails(ifscCode: String): BankBranchResponse {
        return cache.getOrPut(ifscCode) {
            ifscApi.getBankDetails(ifscCode)
        }
    }
}

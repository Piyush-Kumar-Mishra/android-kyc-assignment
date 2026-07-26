package com.example.android_kyc_assignment.data.network

import com.example.android_kyc_assignment.data.model.UsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DummyJsonApi {
    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0,
        @Query("select") select: String = "id,firstName,lastName,image,birthDate,phone,email,address,bank"
    ): UsersResponse
}

package com.example.android_kyc_assignment.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kyc_status")
data class KycEntity(
    @PrimaryKey val userId: Int,
    val isVerified: Boolean = false,
    val selfiePath: String? = null
)

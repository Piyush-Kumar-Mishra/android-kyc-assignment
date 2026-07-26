package com.example.android_kyc_assignment.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KycDao {
    @Query("SELECT * FROM kyc_status")
    suspend fun getAllKycStatuses(): List<KycEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKycStatus(kycEntity: KycEntity)
}

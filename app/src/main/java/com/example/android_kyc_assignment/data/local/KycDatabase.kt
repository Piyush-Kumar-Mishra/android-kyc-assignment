package com.example.android_kyc_assignment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KycEntity::class], version = 1, exportSchema = false)
abstract class KycDatabase : RoomDatabase() {
    abstract fun kycDao(): KycDao
}

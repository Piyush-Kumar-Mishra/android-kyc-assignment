package com.example.android_kyc_assignment.di

import android.content.Context
import androidx.room.Room
import com.example.android_kyc_assignment.data.local.KycDao
import com.example.android_kyc_assignment.data.local.KycDatabase
import com.example.android_kyc_assignment.data.network.DummyJsonApi
import com.example.android_kyc_assignment.data.network.IfscApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDummyJsonApi(): DummyJsonApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DummyJsonApi::class.java)
    }

    @Provides
    @Singleton
    fun provideIfscApi(): IfscApi {
        return Retrofit.Builder()
            .baseUrl("https://ifsc.razorpay.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IfscApi::class.java)
    }

    @Provides
    @Singleton
    fun provideKycDatabase(@ApplicationContext context: Context): KycDatabase {
        return Room.databaseBuilder(
            context,
            KycDatabase::class.java,
            "kyc_database"
        ).build()
    }

    @Provides
    fun provideKycDao(database: KycDatabase): KycDao {
        return database.kycDao()
    }
}

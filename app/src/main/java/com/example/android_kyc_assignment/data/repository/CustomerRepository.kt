package com.example.android_kyc_assignment.data.repository

import com.example.android_kyc_assignment.data.local.KycDao
import com.example.android_kyc_assignment.data.local.KycEntity
import com.example.android_kyc_assignment.data.model.Customer
import com.example.android_kyc_assignment.data.network.DummyJsonApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.round
import kotlin.random.Random

@Singleton
class CustomerRepository @Inject constructor(
    private val dummyJsonApi: DummyJsonApi,
    private val kycDao: KycDao
) {
    private var cachedCustomers: List<Customer> = emptyList()
    private var lastFetchTime: Long = 0
    private val cacheDurationMillis = 1 * 60 * 1000L // 1 min

    private val ifscCodes = listOf("HDFC0CAGSBK", "SBIN0000001", "ICIC0000001", "PUNB0244200", "UTIB0000001")

    suspend fun getCustomers(): List<Customer> {
        val currentTime = System.currentTimeMillis()
        if (cachedCustomers.isNotEmpty() && (currentTime - lastFetchTime) < cacheDurationMillis) {
            return cachedCustomers
        }

        val usersResponse = dummyJsonApi.getUsers()
        val kycStatuses = kycDao.getAllKycStatuses().associateBy { it.userId }

        val customers = usersResponse.users.map { user ->
            val randomGenerator = Random(user.id)
            val balance = round((randomGenerator.nextDouble() * 900000 + 100000) * 100) / 100
            val ifscCode = ifscCodes[user.id % 5]
            val kycStatus = kycStatuses[user.id]

            Customer(
                id = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                imageUrl = user.image,
                birthDate = user.birthDate,
                phone = user.phone,
                email = user.email,
                address = user.address.address,
                city = user.address.city,
                state = user.address.state,
                country = user.address.country,
                cardType = user.bank.cardType,
                currency = user.bank.currency,
                iban = user.bank.iban,
                balance = balance,
                ifscCode = ifscCode,
                isVerified = kycStatus?.isVerified ?: false,
                selfiePath = kycStatus?.selfiePath
            )
        }

        cachedCustomers = customers
        lastFetchTime = currentTime

        return customers
    }

    suspend fun getCustomerById(id: Int): Customer? {
        return getCustomers().find { it.id == id }
    }

    suspend fun markAsVerified(userId: Int, selfiePath: String) {
        val kycEntity = KycEntity(userId = userId, isVerified = true, selfiePath = selfiePath)
        kycDao.insertKycStatus(kycEntity)

        cachedCustomers = cachedCustomers.map {
            if (it.id == userId) {
                it.copy(isVerified = true, selfiePath = selfiePath)
            } else {
                it
            }
        }
    }
}

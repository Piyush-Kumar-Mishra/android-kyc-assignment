package com.example.android_kyc_assignment.data.model

data class Customer(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val birthDate: String,
    val phone: String,
    val email: String,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val cardType: String,
    val currency: String,
    val iban: String,
    val balance: Double,
    val ifscCode: String,
    val isVerified: Boolean = false,
    val selfiePath: String? = null
) {
    val fullName: String get() = "$firstName $lastName"
    
    val maskedIban: String get() {
        if (iban.length <= 4) return iban
        return "**** **** " + iban.takeLast(4)
    }
}

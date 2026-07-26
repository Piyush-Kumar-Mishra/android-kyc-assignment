package com.example.android_kyc_assignment.data.model

data class UsersResponse(
    val users: List<UserDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val image: String,
    val birthDate: String,
    val phone: String,
    val email: String,
    val address: AddressDto,
    val bank: BankDto
)

data class AddressDto(
    val address: String,
    val city: String,
    val state: String,
    val country: String
)

data class BankDto(
    val cardType: String,
    val currency: String,
    val iban: String
)

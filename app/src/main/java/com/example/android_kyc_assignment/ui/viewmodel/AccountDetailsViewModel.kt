package com.example.android_kyc_assignment.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_kyc_assignment.data.model.BankBranchResponse
import com.example.android_kyc_assignment.data.model.Customer
import com.example.android_kyc_assignment.data.repository.CustomerRepository
import com.example.android_kyc_assignment.data.repository.IfscRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val customerRepository: CustomerRepository,
    private val ifscRepository: IfscRepository
) : ViewModel() {

    private val customerId: Int = savedStateHandle.get<Int>("customerId") ?: 0

    private val _uiState = MutableStateFlow(AccountDetailsUiState())
    val uiState: StateFlow<AccountDetailsUiState> = _uiState

    init {
        loadCustomerDetails()
    }

    fun loadCustomerDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val customer = customerRepository.getCustomerById(customerId)
                if (customer != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        customer = customer
                    )
                    loadBankDetails(customer.ifscCode)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Customer not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load customer"
                )
            }
        }
    }

    private fun loadBankDetails(ifscCode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isBankLoading = true, bankError = null)
            try {
                val bankDetails = ifscRepository.getBankDetails(ifscCode)
                _uiState.value = _uiState.value.copy(
                    isBankLoading = false,
                    bankDetails = bankDetails
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isBankLoading = false,
                    bankError = e.message ?: "Failed to load bank details"
                )
            }
        }
    }

    fun markAsVerified(selfiePath: String) {
        viewModelScope.launch {
            try {
                customerRepository.markAsVerified(customerId, selfiePath)
                val updatedCustomer = customerRepository.getCustomerById(customerId)
                _uiState.value = _uiState.value.copy(customer = updatedCustomer)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to save KYC status"
                )
            }
        }
    }
}

data class AccountDetailsUiState(
    val isLoading: Boolean = false,
    val customer: Customer? = null,
    val error: String? = null,
    val isBankLoading: Boolean = false,
    val bankDetails: BankBranchResponse? = null,
    val bankError: String? = null
)

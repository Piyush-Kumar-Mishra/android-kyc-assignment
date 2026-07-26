package com.example.android_kyc_assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_kyc_assignment.data.model.Customer
import com.example.android_kyc_assignment.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountsUiState())
    val uiState: StateFlow<AccountsUiState> = _uiState

    init {
        loadCustomers()
    }

    fun loadCustomers() {
        viewModelScope.launch {
            if (_uiState.value.customers.isEmpty()) {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }
            try {
                val customers = customerRepository.getCustomers()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    customers = customers,
                    error = null
                )
            } catch (e: Exception) {
                if (_uiState.value.customers.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = tabIndex)
    }

    fun getFilteredCustomers(): List<Customer> {
        val state = _uiState.value
        val filtered = if (state.selectedTabIndex == 0) {
            state.customers.filter { it.isVerified }
        } else {
            state.customers.filter { !it.isVerified }
        }

        if (state.searchQuery.isBlank()) return filtered

        return filtered.filter { customer ->
            customer.fullName.contains(state.searchQuery, ignoreCase = true) ||
                    customer.iban.contains(state.searchQuery, ignoreCase = true)
        }
    }
}

data class AccountsUiState(
    val isLoading: Boolean = false,
    val customers: List<Customer> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val selectedTabIndex: Int = 0
)

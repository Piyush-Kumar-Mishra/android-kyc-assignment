package com.example.android_kyc_assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_kyc_assignment.data.model.Customer
import com.example.android_kyc_assignment.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private var searchJob: Job? = null
    private val pageSize = 15

    init {
        loadCustomers()
    }

    fun loadCustomers() {
        viewModelScope.launch {
            if (_uiState.value.customers.isEmpty()) {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }
            try {
                val customers = customerRepository.getCustomers(limit = pageSize, skip = 0)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    customers = customers,
                    currentSkip = 0,
                    isLastPageReached = customers.size < pageSize,
                    error = null
                )
                applyFilters()
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

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoadingMore || state.isLoading || state.isLastPageReached || state.searchQuery.isNotBlank()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMore = true)
            val nextSkip = state.currentSkip + pageSize
            try {
                val previousCount = state.customers.size
                val updatedCustomers = customerRepository.getCustomers(limit = pageSize, skip = nextSkip)
                val newItemsAdded = updatedCustomers.size - previousCount

                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    customers = updatedCustomers,
                    currentSkip = nextSkip,
                    isLastPageReached = newItemsAdded < pageSize
                )
                applyFilters()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoadingMore = false)
            }
        }
    }
    fun refreshCustomers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            try {
                val customers = customerRepository.getCustomers(limit = pageSize, skip = 0, forceRefresh = true)
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    customers = customers,
                    currentSkip = 0,
                    isLastPageReached = customers.size < pageSize,
                    error = null
                )
                applyFilters()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300L)
            applyFilters()
        }
    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = tabIndex)
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val tabFiltered = if (state.selectedTabIndex == 0) {
            state.customers.filter { it.isVerified }
        } else {
            state.customers.filter { !it.isVerified }
        }

        val searchFiltered = if (state.searchQuery.isBlank()) tabFiltered
        else tabFiltered.filter { customer ->
            customer.fullName.contains(state.searchQuery, ignoreCase = true) ||
                    customer.iban.contains(state.searchQuery, ignoreCase = true)
        }

        _uiState.value = _uiState.value.copy(filteredCustomers = searchFiltered)
    }
}

data class AccountsUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val currentSkip: Int = 0,
    val isLastPageReached: Boolean = false,
    val customers: List<Customer> = emptyList(),
    val filteredCustomers: List<Customer> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val selectedTabIndex: Int = 0
)

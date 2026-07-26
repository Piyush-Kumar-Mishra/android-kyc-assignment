package com.example.android_kyc_assignment.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_kyc_assignment.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val customerId: Int = savedStateHandle.get<Int>("customerId") ?: 0

    fun saveSelfie(selfiePath: String, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                customerRepository.markAsVerified(customerId, selfiePath)
                onDone()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

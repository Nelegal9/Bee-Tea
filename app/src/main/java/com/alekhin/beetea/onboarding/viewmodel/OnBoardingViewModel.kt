package com.alekhin.beetea.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alekhin.beetea.onboarding.data.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnBoardingViewModel(private val repository: DataStoreRepository): ViewModel() {
    fun saveOnBoardingState(completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOnBoardingState(completed = completed)
        }
    }
}
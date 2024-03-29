package com.alekhin.beetea.splash.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alekhin.beetea.onboarding.navigation.Screen
import com.alekhin.beetea.onboarding.data.DataStoreRepository
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: DataStoreRepository): ViewModel() {
    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(Screen.Welcome.route)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch { repository.readOnBoardingState().collect { completed ->
                if (completed) _startDestination.value = Screen.Home.route else _startDestination.value = Screen.Welcome.route
            }
            _isLoading.value = false
        }
    }
}
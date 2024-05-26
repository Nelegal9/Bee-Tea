package com.alekhin.beetea.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alekhin.beetea.domain.BluetoothController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BluetoothViewModel(private val bluetoothController: BluetoothController) : ViewModel() {
    private val _state = MutableStateFlow(BluetoothUiState())
    val state = combine(
        _state,
        bluetoothController.pairedDevices,
        bluetoothController.scannedDevices
    ) { state, pairedDevices, scannedDevices ->
        state.copy(pairedDevices = pairedDevices, scannedDevices = scannedDevices)
    }.stateIn(viewModelScope, WhileSubscribed(3000), _state.value)

    fun scanDevices() {
        bluetoothController.scanDevices()
    }

    fun refreshScan() {
        bluetoothController.refreshDevices()
    }
}
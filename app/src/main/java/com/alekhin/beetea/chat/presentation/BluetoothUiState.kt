package com.alekhin.beetea.chat.presentation

import com.alekhin.beetea.chat.domain.BluetoothDevice
import com.alekhin.beetea.chat.domain.BluetoothMessage

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<BluetoothMessage> = emptyList()
)

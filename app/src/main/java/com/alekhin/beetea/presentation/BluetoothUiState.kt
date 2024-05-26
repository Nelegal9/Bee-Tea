package com.alekhin.beetea.presentation

import com.alekhin.beetea.domain.BluetoothDomainDevice
import com.alekhin.beetea.domain.BluetoothMessage

data class BluetoothUiState(
    val pairedDevices: List<BluetoothDomainDevice> = emptyList(),
    val scannedDevices: List<BluetoothDomainDevice> = emptyList(),
    val roomName: String? = "Unknown device",
    val createdRoom: Boolean = false,
    val connecting: Boolean = false,
    val connected: Boolean = false,
    val messages: List<BluetoothMessage> = emptyList(),
    val error: String? = null
)
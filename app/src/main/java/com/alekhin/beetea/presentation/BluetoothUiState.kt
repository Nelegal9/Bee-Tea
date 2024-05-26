package com.alekhin.beetea.presentation

import com.alekhin.beetea.domain.BluetoothDomainDevice

data class BluetoothUiState(
    val pairedDevices: List<BluetoothDomainDevice> = emptyList(),
    val scannedDevices: List<BluetoothDomainDevice> = emptyList(),
    val roomName: String? = "Unknown device",
    val createdRoom: Boolean = false,
    val connecting: Boolean = false,
    val connected: Boolean = false,
    val error: String? = null
)
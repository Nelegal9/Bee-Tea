package com.alekhin.beetea.presentation

import com.alekhin.beetea.domain.BluetoothDomainDevice

data class BluetoothUiState(
    val pairedDevices: List<BluetoothDomainDevice> = emptyList(),
    val scannedDevices: List<BluetoothDomainDevice> = emptyList()
)
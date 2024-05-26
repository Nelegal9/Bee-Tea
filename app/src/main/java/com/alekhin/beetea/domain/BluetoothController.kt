package com.alekhin.beetea.domain

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val pairedDevices: StateFlow<List<BluetoothDomainDevice>>
    val scannedDevices: StateFlow<List<BluetoothDomainDevice>>

    fun scanDevices()
    fun refreshDevices()
}
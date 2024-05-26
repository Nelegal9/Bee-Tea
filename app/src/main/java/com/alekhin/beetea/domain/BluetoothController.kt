package com.alekhin.beetea.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val pairedDevices: StateFlow<List<BluetoothDomainDevice>>
    val scannedDevices: StateFlow<List<BluetoothDomainDevice>>

    val connected: StateFlow<Boolean>
    val errors: SharedFlow<String>

    fun scanDevices()
    fun refreshDevices()

    fun createRoom(): Flow<ConnectionResult>
    fun connectToDevice(device: BluetoothDomainDevice): Flow<ConnectionResult>
    fun closeConnection()
    fun release()

    suspend fun deliverMessage(message: String): BluetoothMessage?
}
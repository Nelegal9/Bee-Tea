package com.alekhin.beetea.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alekhin.beetea.domain.BluetoothController
import com.alekhin.beetea.domain.BluetoothDomainDevice
import com.alekhin.beetea.domain.ConnectionResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class BluetoothViewModel(private val bluetoothController: BluetoothController) : ViewModel() {
    private val _state = MutableStateFlow(BluetoothUiState())
    val state = combine(
        _state,
        bluetoothController.pairedDevices,
        bluetoothController.scannedDevices
    ) { state, pairedDevices, scannedDevices ->
        state.copy(pairedDevices = pairedDevices, scannedDevices = scannedDevices)
    }.stateIn(viewModelScope, WhileSubscribed(3000), _state.value)

    private var deviceConnectionJob: Job? = null

    init {
        bluetoothController.connected.onEach { connected -> _state.update { it.copy(connected = connected) } }.launchIn(viewModelScope)
        bluetoothController.errors.onEach { error -> _state.update { it.copy(error = error) } }.launchIn(viewModelScope)
    }

    fun scanDevices() {
        bluetoothController.scanDevices()
    }

    fun refreshScan() {
        bluetoothController.refreshDevices()
    }

    fun createRoom() {
        _state.update { it.copy(createdRoom = true, connecting = true) }
        deviceConnectionJob = bluetoothController.createRoom().listen()
    }

    fun connectToDevice(device: BluetoothDomainDevice) {
        _state.update { it.copy(roomName = device.name, connecting = true) }
        deviceConnectionJob = bluetoothController.connectToDevice(device).listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update { it.copy(roomName = "Unknown device", connecting = false, connected = false) }
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when(result) {
                ConnectionResult.ConnectionEstablished -> { _state.update { it.copy(connected = true, connecting = false, error = null) } }
                is ConnectionResult.Error -> { _state.update { it.copy(connected = false, connecting = false, error = result.message) } }
            }
        }.catch {
            bluetoothController.closeConnection()
            _state.update { it.copy(connected = false, connecting = false) } }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }
}
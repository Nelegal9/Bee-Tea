package com.alekhin.beetea.data

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice.ACTION_FOUND
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import com.alekhin.beetea.domain.BluetoothController
import com.alekhin.beetea.domain.BluetoothDomainDevice
import com.alekhin.beetea.domain.toBluetoothDeviceDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@SuppressLint("MissingPermission")
class AndroidBluetoothController(private val context: Context) : BluetoothController {
    private val bluetoothManager by lazy { context.getSystemService(BluetoothManager::class.java) }
    private val bluetoothAdapter by lazy { bluetoothManager?.adapter }

    private val _pairedDevices = MutableStateFlow<List<BluetoothDomainDevice>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDomainDevice>>
        get() = _pairedDevices.asStateFlow()
    private val _scannedDevices = MutableStateFlow<List<BluetoothDomainDevice>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDomainDevice>>
        get() = _scannedDevices.asStateFlow()

    private val receiver = BluetoothDeviceReceiver { device ->
        _scannedDevices.update { devices ->
            val newDevice = device.toBluetoothDeviceDomain()
            if (newDevice in _pairedDevices.value || newDevice in devices) devices else devices + newDevice
        }
    }
    private val filter = IntentFilter(ACTION_FOUND)

    init { queryPairedDevices() }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PERMISSION_GRANTED
    }

    private fun queryPairedDevices() {
        if (!hasPermission(BLUETOOTH_CONNECT)) return
        bluetoothAdapter?.bondedDevices?.map { it.toBluetoothDeviceDomain() }?.also { devices ->
            _pairedDevices.update { devices }
        }
    }

    override fun scanDevices() {
        if (!hasPermission(BLUETOOTH_SCAN)) return
        context.registerReceiver(receiver, filter)
        queryPairedDevices()
        bluetoothAdapter?.startDiscovery()
    }

    override fun refreshDevices() {
        if (!hasPermission(BLUETOOTH_SCAN)) return
        bluetoothAdapter?.cancelDiscovery()
        if (bluetoothAdapter?.isEnabled == true) {
            _scannedDevices.update { emptyList() }
            scanDevices()
        } else {
            _pairedDevices.update { emptyList() }
            _scannedDevices.update { emptyList() }
        }
    }
}
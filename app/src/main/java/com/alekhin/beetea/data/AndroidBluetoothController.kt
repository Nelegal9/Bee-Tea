package com.alekhin.beetea.data

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED
import android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED
import android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED
import android.bluetooth.BluetoothDevice.ACTION_FOUND
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import com.alekhin.beetea.domain.BluetoothController
import com.alekhin.beetea.domain.BluetoothDomainDevice
import com.alekhin.beetea.domain.ConnectionResult
import com.alekhin.beetea.domain.toBluetoothDeviceDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID.fromString

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

    private val _connected = MutableStateFlow(false)
    override val connected: StateFlow<Boolean>
        get() = _connected.asStateFlow()

    private val _errors = MutableSharedFlow<String>()
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    private val receiver = BluetoothDeviceReceiver { device ->
        _scannedDevices.update { devices ->
            val newDevice = device.toBluetoothDeviceDomain()
            if (newDevice in _pairedDevices.value || newDevice in devices) devices else devices + newDevice
        }
    }
    private val filter = IntentFilter(ACTION_FOUND)

    private val stateReceiver = BluetoothStateReceiver { connected, bluetoothDevice ->
        if(bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == true) _connected.update { connected } else CoroutineScope(IO).launch { _errors.emit("Can't connect to a non-paired device") }
    }
    private val stateFilter = IntentFilter().apply {
        addAction(ACTION_CONNECTION_STATE_CHANGED)
        addAction(ACTION_ACL_CONNECTED)
        addAction(ACTION_ACL_DISCONNECTED)
    }

    private var serverSocket: BluetoothServerSocket? = null
    private var clientSocket: BluetoothSocket? = null

    init { context.registerReceiver(stateReceiver, stateFilter) }

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

    override fun createRoom(): Flow<ConnectionResult> {
        return flow {
            if (!hasPermission(BLUETOOTH_CONNECT)) throw SecurityException("No BLUETOOTH_CONNECT permission")
            serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(SERVICE_NAME, fromString(SERVICE_UUID))
            var shouldLoop = true
            while (shouldLoop) {
                clientSocket = try { serverSocket?.accept() } catch (e: IOException) {
                    shouldLoop = false
                    null
                }

                emit(ConnectionResult.ConnectionEstablished)
                clientSocket?.let {
                    serverSocket?.close()
                    if (clientSocket?.isConnected == false) return@flow
                    while (true) { /* TODO: Manage connected socket. */ }
                }
            }
        }.onCompletion { closeConnection() }.flowOn(IO)
    }

    override fun connectToDevice(device: BluetoothDomainDevice): Flow<ConnectionResult> {
        return flow {
            if (!hasPermission(BLUETOOTH_CONNECT)) throw SecurityException("No BLUETOOTH_CONNECT permission")
            clientSocket = bluetoothAdapter?.getRemoteDevice(device.address)?.createRfcommSocketToServiceRecord(fromString(SERVICE_UUID))
            bluetoothAdapter?.cancelDiscovery()
            clientSocket?.let {
                try {
                    it.connect()
                    emit(ConnectionResult.ConnectionEstablished)
                    if (clientSocket?.isConnected == false) return@flow
                    while (true) { /* TODO: Manage connected socket. */ }
                } catch (e: IOException) {
                    it.close()
                    emit(ConnectionResult.Error("Connection was interrupted"))
                    clientSocket = null
                }
            }
        }.onCompletion { closeConnection() }.flowOn(IO)
    }

    override fun closeConnection() {
        serverSocket?.close()
        serverSocket = null
        clientSocket?.close()
        clientSocket = null
    }

    override fun release() {
        context.unregisterReceiver(receiver)
        context.unregisterReceiver(stateReceiver)
        closeConnection()
    }

    companion object {
        const val SERVICE_NAME: String = "chat_service"
        const val SERVICE_UUID: String = "018fb388-a481-7aaf-b57e-05d39ae23e06"
    }
}
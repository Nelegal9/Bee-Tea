package com.alekhin.beetea

import android.Manifest.permission.BLUETOOTH_ADVERTISE
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.net.Uri.fromParts
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alekhin.beetea.R.string.app_name
import com.alekhin.beetea.R.string.connected
import com.alekhin.beetea.R.string.create_room
import com.alekhin.beetea.presentation.BluetoothViewModel
import com.alekhin.beetea.presentation.components.ConnectScreen
import com.alekhin.beetea.presentation.components.DeviceScreen
import com.alekhin.beetea.presentation.components.PermissionDialog
import com.alekhin.beetea.ui.theme.BeeTeaTheme

@ExperimentalMaterial3Api
@SuppressLint("MissingPermission")
class MainActivity : ComponentActivity() {
    private val bluetoothManager: BluetoothManager by lazy { getSystemService(BluetoothManager::class.java) }
    private val bluetoothAdapter: BluetoothAdapter? by lazy { bluetoothManager.adapter }

    private val permissionsToRequest = arrayOf(BLUETOOTH_SCAN, BLUETOOTH_ADVERTISE, BLUETOOTH_CONNECT)
    private val permissionDialogQueue = mutableStateListOf<String>()

    private val bluetoothViewModel: BluetoothViewModel = BluetoothViewModel(BeeTeaApplication.bluetoothAppModule.provideBluetoothController)
    private var bluetoothAdapterState = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val enableBtLauncher = registerForActivityResult(StartActivityForResult()) { if (bluetoothAdapter?.isEnabled == true) bluetoothAdapterState.value = true }
        val enableDiscoverLauncher = registerForActivityResult(StartActivityForResult()) { bluetoothAdapterState.value = bluetoothAdapter?.scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE }
        val enablePermsLauncher = registerForActivityResult(RequestMultiplePermissions()) { perms ->
            if (perms[BLUETOOTH_CONNECT] == false && !permissionDialogQueue.contains(BLUETOOTH_CONNECT)) permissionDialogQueue.add(BLUETOOTH_CONNECT)
            if (perms[BLUETOOTH_CONNECT] == true) if (bluetoothAdapterState.value) enableBtLauncher.launch(Intent(ACTION_REQUEST_ENABLE)) else enableDiscoverLauncher.launch(Intent(ACTION_REQUEST_DISCOVERABLE))
        }.also { it.launch(permissionsToRequest) }

        installSplashScreen()
        setContent {
            val state by bluetoothViewModel.state.collectAsState()

            LaunchedEffect(key1 = state.connected) { if (state.connected) { makeText(applicationContext, connected, LENGTH_LONG).show() } }
            LaunchedEffect(key1 = state.error) { state.error?.let { message -> makeText(applicationContext, message, LENGTH_LONG).show() } }

            BeeTeaTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text(text = stringResource(id = app_name)) }) },
                    bottomBar = { /* TODO: Add bottom bar. */ },
                    floatingActionButton = { if (!state.connecting && !state.connected) ExtendedFloatingActionButton(
                        text = { Text(text = stringResource(id = create_room)) },
                        icon = { Icon(imageVector = Icons.Default.Create, contentDescription = stringResource(id = create_room)) },
                        onClick = bluetoothViewModel::createRoom) }
                ) { paddingValues ->
                    when {
                        state.connected -> { Column(modifier = Modifier.padding(paddingValues)) { /* TODO: Add chat screen. */ } }
                        state.connecting -> { ConnectScreen(createdRoom = state.createdRoom, state = state, onCancelClick = bluetoothViewModel::disconnectFromDevice) }
                        else -> {
                            DeviceScreen(modifier = Modifier.padding(paddingValues), state = state, onRefreshClick = bluetoothViewModel::refreshScan, onDeviceClick = bluetoothViewModel::connectToDevice)
                            if (bluetoothAdapter?.isEnabled == true || bluetoothAdapterState.value) bluetoothViewModel.scanDevices()
                        }
                    }
                }

                permissionDialogQueue.reversed().forEach { permission ->
                    PermissionDialog(
                        permanentlyDeclined = !shouldShowRequestPermissionRationale(permission),
                        onDismiss = { permissionDialogQueue.removeFirst() },
                        onOkClick = {
                            permissionDialogQueue.removeFirst()
                            enablePermsLauncher.launch(permissionsToRequest)
                        },
                        onVisitAppSettingsClick = ::openAppSettings
                    )
                }
            }
        }
    }
}

fun Activity.openAppSettings() {
    Intent(ACTION_APPLICATION_DETAILS_SETTINGS, fromParts("package", packageName, null)).also(::startActivity)
}
package com.alekhin.beetea

import android.Manifest.permission.BLUETOOTH_ADVERTISE
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.net.Uri.fromParts
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alekhin.beetea.R.string.app_name
import com.alekhin.beetea.presentation.BluetoothViewModel
import com.alekhin.beetea.presentation.components.DeviceScreen
import com.alekhin.beetea.presentation.components.PermissionDialog
import com.alekhin.beetea.ui.theme.BeeTeaTheme

@ExperimentalMaterial3Api
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
        val enablePermsLauncher = registerForActivityResult(RequestMultiplePermissions()) { perms ->
            if (perms[BLUETOOTH_CONNECT] == false && !permissionDialogQueue.contains(BLUETOOTH_CONNECT)) permissionDialogQueue.add(BLUETOOTH_CONNECT)
            if (perms[BLUETOOTH_CONNECT] == true) if (bluetoothAdapter?.isEnabled == false) enableBtLauncher.launch(Intent(ACTION_REQUEST_ENABLE))
        }.also { it.launch(permissionsToRequest) }

        installSplashScreen()
        setContent {
            val state by bluetoothViewModel.state.collectAsState()

            BeeTeaTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text(text = stringResource(id = app_name)) }) },
                    bottomBar = { /* TODO: Add bottom bar. */ },
                    floatingActionButton = { /* TODO: Add extended floating action button. */  }
                ) { paddingValues ->
                    DeviceScreen(modifier = Modifier.padding(paddingValues), state = state, onRefreshClick = bluetoothViewModel::refreshScan, onDeviceClick = { /* TODO: Add onDeviceClick. */ })
                    if (bluetoothAdapter?.isEnabled == true || bluetoothAdapterState.value) bluetoothViewModel.scanDevices()
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
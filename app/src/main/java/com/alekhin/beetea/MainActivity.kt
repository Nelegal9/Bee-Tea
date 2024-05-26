package com.alekhin.beetea

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alekhin.beetea.R.string.app_name
import com.alekhin.beetea.ui.theme.BeeTeaTheme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    private val bluetoothManager: BluetoothManager by lazy { getSystemService(BluetoothManager::class.java) }
    private val bluetoothAdapter: BluetoothAdapter? by lazy { bluetoothManager.adapter }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val enableBtLauncher = registerForActivityResult(StartActivityForResult()) { /* TODO: Handle ActivityResult. */ }
        registerForActivityResult(RequestPermission()) { granted ->
            if (granted) if (bluetoothAdapter?.isEnabled == false) enableBtLauncher.launch(Intent(ACTION_REQUEST_ENABLE))
        }.also { it.launch(BLUETOOTH_CONNECT) }

        installSplashScreen()
        setContent {
            BeeTeaTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text(text = stringResource(id = app_name)) }) },
                    bottomBar = { /* TODO: Add bottom bar. */ },
                    floatingActionButton = { /* TODO: Add extended floating action button. */  }
                ) { paddingValues ->
                    Text(text = stringResource(id = app_name), modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}
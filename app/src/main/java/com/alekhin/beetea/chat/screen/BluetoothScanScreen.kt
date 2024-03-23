package com.alekhin.beetea.chat.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alekhin.beetea.MyApplication
import com.alekhin.beetea.chat.BluetoothDevice
import com.alekhin.beetea.chat.viewmodel.BluetoothViewModel
import com.alekhin.beetea.onboarding.viewmodel.viewModelFactory

@ExperimentalComposeUiApi
@Composable
fun BluetoothScanScreen(bluetoothViewModel: BluetoothViewModel = viewModel<BluetoothViewModel>(
    factory = viewModelFactory { BluetoothViewModel(MyApplication.bluetoothModule.provideBluetoothController) }
)) {
    val state by bluetoothViewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = state.errorMessage) {
        state.errorMessage?.let { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // TEST
    LaunchedEffect(key1 = state.isConnected) {
        if (state.isConnected) {
            Toast.makeText(
                context,
                "You're connected!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    // TEST

    when {
        state.isConnecting -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Text(text = "Connecting...")
            }
        }

        state.isConnected -> {
            BluetoothChatScreen()
        }

        else -> {
            Column(modifier = Modifier.fillMaxSize()) {
                BluetoothDeviceList(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    pairedDevices = state.pairedDevices,
                    scannedDevices = state.scannedDevices,
                    bluetoothViewModel = bluetoothViewModel
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StartScanButton(modifier = Modifier) {
                        bluetoothViewModel.startScan()
                    }
                    StopScanButton(modifier = Modifier) {
                        bluetoothViewModel.stopScan()
                    }
                    StartServerButton(modifier = Modifier) {
                        bluetoothViewModel.waitForIncomingConnections()
                    }
                }
            }
        }
    }
}

@Composable
fun BluetoothDeviceList(
    modifier: Modifier,
    pairedDevices: List<BluetoothDevice>,
    scannedDevices: List<BluetoothDevice>,
    bluetoothViewModel: BluetoothViewModel
) {
    LazyColumn(modifier = modifier) {
        item {
            Text(modifier = Modifier.padding(16.dp),
                text = "Paired Devices",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
        items(pairedDevices) { device ->
            Text(
                modifier = Modifier
                    .clickable { bluetoothViewModel.connectToDevice(device) }
                    .fillMaxWidth()
                    .padding(16.dp),
                text = device.name ?: "(No name)")
        }
        item {
            Text(modifier = Modifier.padding(16.dp),
                text = "Scanned Devices",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
        items(scannedDevices) { device ->
            Text(
                modifier = Modifier
                    .clickable { bluetoothViewModel.connectToDevice(device) }
                    .fillMaxWidth()
                    .padding(16.dp),
                text = device.name ?: "(No name)")
        }
    }
}

@Composable
fun StartScanButton(
    modifier: Modifier,
    onStartScan: () -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = 40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Button(
            onClick = onStartScan,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White
            )
        ) {
            Text(text = "Start scan")
        }
    }
}

@Composable
fun StopScanButton(
    modifier: Modifier,
    onStopScan: () -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = 40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Button(
            onClick = onStopScan,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White
            )
        ) {
            Text(text = "Stop scan")
        }
    }
}

@Composable
fun StartServerButton(
    modifier: Modifier,
    onStartServer: () -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = 40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Button(
            onClick = onStartServer,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White
            )
        ) {
            Text(text = "Start server")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BluetoothScanScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        // TODO: Add preview.
    }
}
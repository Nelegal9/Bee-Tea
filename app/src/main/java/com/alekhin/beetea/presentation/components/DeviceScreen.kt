package com.alekhin.beetea.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alekhin.beetea.R.string.available_devices
import com.alekhin.beetea.R.string.paired_devices
import com.alekhin.beetea.R.string.refresh_devices
import com.alekhin.beetea.R.string.scanned_devices
import com.alekhin.beetea.R.string.unknown_device
import com.alekhin.beetea.domain.BluetoothDomainDevice
import com.alekhin.beetea.presentation.BluetoothUiState
import com.alekhin.beetea.ui.theme.BeeTeaTypography

@Composable
fun DeviceScreen(modifier: Modifier = Modifier, state: BluetoothUiState, onRefreshClick: () -> Unit, onDeviceClick: (BluetoothDomainDevice) -> Unit) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = stringResource(id = available_devices), modifier = Modifier.align(CenterVertically).padding(end = 16.dp).weight(1.0f), style = BeeTeaTypography.labelLarge)
            FilledIconButton(onClick = onRefreshClick) { Icon(imageVector = Default.Refresh, contentDescription = stringResource(id = refresh_devices)) }
        }
        DeviceList(pairedDevices = state.pairedDevices, scannedDevices = state.scannedDevices, onDeviceClick = onDeviceClick)
    }
}

@Composable
fun DeviceList(pairedDevices: List<BluetoothDomainDevice>, scannedDevices: List<BluetoothDomainDevice>, onDeviceClick: (BluetoothDomainDevice) -> Unit) {
    LazyColumn {
        item { Text(text = stringResource(id = paired_devices), modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), style = BeeTeaTypography.titleLarge) }
        items(pairedDevices) { device ->
            Device(deviceName = device.name, deviceAddress = device.address, onClick = { onDeviceClick(device) })
        }
        item { HorizontalDivider(modifier = Modifier.padding(16.dp)) }
        item { Text(text = stringResource(id = scanned_devices), modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), style = BeeTeaTypography.titleLarge) }
        items(scannedDevices) { device ->
            Device(deviceName = device.name, deviceAddress = device.address, onClick = { onDeviceClick(device) })
        }
    }
}

@Composable
fun Device(deviceName: String?, deviceAddress: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 24.dp, vertical = 16.dp)) {
        Text(text = deviceName ?: stringResource(id = unknown_device))
        Text(text = deviceAddress, style = BeeTeaTypography.bodyMedium)
    }
}
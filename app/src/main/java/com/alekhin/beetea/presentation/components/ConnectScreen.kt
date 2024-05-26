package com.alekhin.beetea.presentation.components

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alekhin.beetea.R.string.cancel
import com.alekhin.beetea.R.string.connecting
import com.alekhin.beetea.R.string.room
import com.alekhin.beetea.R.string.waiting
import com.alekhin.beetea.presentation.BluetoothUiState
import com.alekhin.beetea.ui.theme.BeeTeaTypography

@Composable
fun ConnectScreen(createdRoom: Boolean = false, state: BluetoothUiState, onCancelClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Center, horizontalAlignment = CenterHorizontally) {
        CircularProgressIndicator()
        Text(text = if (createdRoom) stringResource(id = waiting) else "${stringResource(id = connecting)} ${state.roomName} ${stringResource(id = room)}…", modifier = Modifier.padding(vertical = 32.dp), style = BeeTeaTypography.titleLarge)
        TextButton(onClick = onCancelClick) {
            Text(text = stringResource(id = cancel))
        }
    }
}
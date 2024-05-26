package com.alekhin.beetea.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alekhin.beetea.R.string.close_room
import com.alekhin.beetea.R.string.room
import com.alekhin.beetea.domain.BluetoothMessage
import com.alekhin.beetea.presentation.BluetoothUiState
import com.alekhin.beetea.ui.theme.BeeTeaTypography

@Composable
fun ChatScreen(modifier: Modifier = Modifier, roomName: String?, state: BluetoothUiState, onCloseRoomClick: () -> Unit) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            IconButton(onClick = onCloseRoomClick) { Icon(imageVector = Default.Close, contentDescription = stringResource(id = close_room)) }
            Text(text = "$roomName ${stringResource(id = room)}", modifier = Modifier.align(CenterVertically).padding(start = 16.dp).weight(1.0f), style = BeeTeaTypography.labelLarge)
        }
        MessageList(state = state)
    }
}

@Composable
fun MessageList(state: BluetoothUiState) {
    LazyColumn {
        items(state.messages) { message ->
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)) {
                Message(modifier = Modifier.align(if (message.fromLocalUser) End else Start), message = message)
            }
        }
    }
}

@Composable
fun Message(modifier: Modifier = Modifier, message: BluetoothMessage) {
    Column(modifier = modifier
        .clip(
            shape = RoundedCornerShape(
                topStart = if (message.fromLocalUser) 28.dp else 2.dp,
                topEnd = 28.dp,
                bottomEnd = if (message.fromLocalUser) 2.dp else 28.dp,
                bottomStart = 28.dp
            )
        )
        .background(if (message.fromLocalUser) colorScheme.inversePrimary else colorScheme.surfaceVariant)
        .padding(horizontal = 24.dp, vertical = 16.dp)) {
        Text(text = message.senderName, modifier = Modifier.align(if (message.fromLocalUser) End else Start))
        Text(text = message.message, modifier = Modifier.align(if (message.fromLocalUser) End else Start) , style = BeeTeaTypography.bodyMedium)
    }
}
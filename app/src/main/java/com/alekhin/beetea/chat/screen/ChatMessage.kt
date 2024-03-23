package com.alekhin.beetea.chat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alekhin.beetea.chat.BluetoothMessage

@Composable
fun ChatMessage(modifier: Modifier = Modifier, message: BluetoothMessage) {
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = if (message.isFromLocalUser) 16.dp else 0.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = if (message.isFromLocalUser) 0.dp else 16.dp
                )
            )
            .background(
                if (message.isFromLocalUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
            .padding(16.dp)
    ) {
        Text(
            text = message.senderName,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = message.message,
            modifier = Modifier.widthIn(max = 250.dp),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageSendPreview() {
    ChatMessage(message = BluetoothMessage(
        message = "Hello chat!",
        senderName = "Redmi 10C",
        isFromLocalUser = true
    ))
}

@Preview(showBackground = true)
@Composable
fun ChatMessageReceivePreview() {
    ChatMessage(message = BluetoothMessage(
        message = "Hello chat!",
        senderName = "Redmi 12",
        isFromLocalUser = false
    ))
}
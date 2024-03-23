package com.alekhin.beetea.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScanButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    Row(
        modifier = modifier.padding(vertical = 40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(contentColor = Color.White)
        ) { Text(text = text) }
    }
}
package com.alekhin.beetea.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alekhin.beetea.R.string.ok
import com.alekhin.beetea.R.string.permission_required
import com.alekhin.beetea.R.string.permission_requirement_declined_description
import com.alekhin.beetea.R.string.permission_requirement_description
import com.alekhin.beetea.R.string.visit_app_settings

@Composable
fun PermissionDialog(permanentlyDeclined: Boolean, onDismiss: () -> Unit, onOkClick: () -> Unit, onVisitAppSettingsClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = if (!permanentlyDeclined) onOkClick else onVisitAppSettingsClick) { Text(text = if (!permanentlyDeclined) stringResource(id = ok) else stringResource(id = visit_app_settings)) } },
        icon = { /* TODO: Add Bluetooth hero icon. */ },
        title = { Text(text = stringResource(id = permission_required)) },
        text = { Text(text = if (!permanentlyDeclined) stringResource(id = permission_requirement_description) else stringResource(id = permission_requirement_declined_description)) }
    )
}
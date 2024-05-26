package com.alekhin.beetea.data

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED
import android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED
import android.bluetooth.BluetoothDevice.EXTRA_DEVICE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothStateReceiver(private val onStateChanged: (connected: Boolean, BluetoothDevice) -> Unit): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action: String? = intent?.action
        val device = intent?.getParcelableExtra(EXTRA_DEVICE, BluetoothDevice::class.java)
        when(action) {
            ACTION_ACL_CONNECTED -> { onStateChanged(true, device ?: return) }
            ACTION_ACL_DISCONNECTED -> { onStateChanged(false, device ?: return) }
        }
    }
}
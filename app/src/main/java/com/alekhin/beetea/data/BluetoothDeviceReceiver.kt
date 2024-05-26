package com.alekhin.beetea.data

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_FOUND
import android.bluetooth.BluetoothDevice.EXTRA_DEVICE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothDeviceReceiver(private val receiver: (BluetoothDevice) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action: String? = intent?.action

        when (action) {
            ACTION_FOUND -> {
                val device: BluetoothDevice? = intent.getParcelableExtra(EXTRA_DEVICE, BluetoothDevice::class.java)
                device?.let(receiver)
            }
        }
    }
}
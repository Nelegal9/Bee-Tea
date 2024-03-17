package com.alekhin.beetea.chat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceChat(): BluetoothDeviceChat {
    return BluetoothDeviceChat(
        name = name,
        address = address
    )
}
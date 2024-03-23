package com.alekhin.beetea.chat.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.alekhin.beetea.chat.domain.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}
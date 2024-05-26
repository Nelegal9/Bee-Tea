package com.alekhin.beetea.domain

import android.annotation.SuppressLint

data class BluetoothDomainDevice(val name: String?, val address: String)

@SuppressLint("MissingPermission")
fun android.bluetooth.BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDomainDevice {
    return BluetoothDomainDevice(name, address)
}
package com.alekhin.beetea.chat.di

import com.alekhin.beetea.chat.BluetoothController

interface BluetoothModule {
    val provideBluetoothController: BluetoothController
}
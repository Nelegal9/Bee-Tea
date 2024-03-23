package com.alekhin.beetea.chat.di

import com.alekhin.beetea.chat.domain.BluetoothController

interface BluetoothModule {
    val provideBluetoothController: BluetoothController
}
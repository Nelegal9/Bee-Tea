package com.alekhin.beetea.di

import com.alekhin.beetea.domain.BluetoothController

interface BluetoothAppModule {
    val provideBluetoothController: BluetoothController
}
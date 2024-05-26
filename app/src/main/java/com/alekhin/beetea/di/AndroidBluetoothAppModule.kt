package com.alekhin.beetea.di

import android.content.Context
import com.alekhin.beetea.data.AndroidBluetoothController
import com.alekhin.beetea.domain.BluetoothController

class AndroidBluetoothAppModule(private val context: Context) : BluetoothAppModule {
    override val provideBluetoothController: BluetoothController by lazy { AndroidBluetoothController(context = context) }
}
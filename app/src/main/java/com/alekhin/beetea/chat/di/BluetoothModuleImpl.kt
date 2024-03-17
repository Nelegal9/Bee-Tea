package com.alekhin.beetea.chat.di

import android.content.Context
import com.alekhin.beetea.chat.BluetoothController
import com.alekhin.beetea.chat.BluetoothControllerImpl

class BluetoothModuleImpl(private val context: Context): BluetoothModule {
    override val provideBluetoothController: BluetoothController by lazy {
        BluetoothControllerImpl(context = context)
    }
}
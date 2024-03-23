package com.alekhin.beetea.chat.di

import android.content.Context
import com.alekhin.beetea.chat.domain.BluetoothController
import com.alekhin.beetea.chat.data.BluetoothControllerImpl

class BluetoothModuleImpl(private val context: Context): BluetoothModule {
    override val provideBluetoothController: BluetoothController by lazy { BluetoothControllerImpl(context = context) }
}
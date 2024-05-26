package com.alekhin.beetea

import android.app.Application
import com.alekhin.beetea.di.AndroidBluetoothAppModule
import com.alekhin.beetea.di.BluetoothAppModule

class BeeTeaApplication : Application() {
    companion object { lateinit var bluetoothAppModule: BluetoothAppModule }

    override fun onCreate() {
        super.onCreate()
        bluetoothAppModule = AndroidBluetoothAppModule(context = this)
    }
}
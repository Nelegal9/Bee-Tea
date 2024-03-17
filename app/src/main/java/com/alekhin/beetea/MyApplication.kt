package com.alekhin.beetea

import android.app.Application
import com.alekhin.beetea.chat.di.BluetoothModule
import com.alekhin.beetea.chat.di.BluetoothModuleImpl
import com.alekhin.beetea.onboarding.di.MainModule
import com.alekhin.beetea.onboarding.di.MainModuleImpl

class MyApplication: Application() {
    companion object {
        lateinit var mainModule: MainModule
        lateinit var bluetoothModule: BluetoothModule
    }

    override fun onCreate() {
        super.onCreate()
        mainModule = MainModuleImpl(context = this)
        bluetoothModule = BluetoothModuleImpl(context = this)
    }
}
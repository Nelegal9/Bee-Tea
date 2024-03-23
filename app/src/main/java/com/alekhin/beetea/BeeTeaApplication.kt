package com.alekhin.beetea

import android.app.Application
import com.alekhin.beetea.chat.di.BluetoothModule
import com.alekhin.beetea.chat.di.BluetoothModuleImpl
import com.alekhin.beetea.onboarding.di.OnboardingModule
import com.alekhin.beetea.onboarding.di.OnboardingModuleImpl

class BeeTeaApplication: Application() {
    companion object {
        lateinit var onboardingModule: OnboardingModule
        lateinit var bluetoothModule: BluetoothModule
    }

    override fun onCreate() {
        super.onCreate()
        onboardingModule = OnboardingModuleImpl(context = this)
        bluetoothModule = BluetoothModuleImpl(context = this)
    }
}
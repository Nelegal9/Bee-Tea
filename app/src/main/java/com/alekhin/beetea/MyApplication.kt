package com.alekhin.beetea

import android.app.Application
import com.alekhin.beetea.onboarding.di.MainModule
import com.alekhin.beetea.onboarding.di.MainModuleImpl

class MyApplication: Application() {
    companion object {
        lateinit var mainModule: MainModule
    }

    override fun onCreate() {
        super.onCreate()
        mainModule = MainModuleImpl(context = this)
    }
}
package com.alekhin.beetea.onboarding.di

import android.content.Context
import com.alekhin.beetea.onboarding.data.DataStoreRepository

class MainModuleImpl(private val context: Context): MainModule {
    override val provideDataStoreRepository: DataStoreRepository by lazy {
        DataStoreRepository(context = context)
    }
}
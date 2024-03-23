package com.alekhin.beetea.onboarding.di

import android.content.Context
import com.alekhin.beetea.onboarding.data.DataStoreRepository

class OnboardingModuleImpl(private val context: Context): OnboardingModule {
    override val provideDataStoreRepository: DataStoreRepository by lazy { DataStoreRepository(context = context) }
}
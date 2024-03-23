package com.alekhin.beetea.onboarding.di

import com.alekhin.beetea.onboarding.data.DataStoreRepository

interface OnboardingModule {
    val provideDataStoreRepository: DataStoreRepository
}
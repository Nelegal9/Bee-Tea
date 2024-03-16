package com.alekhin.beetea.onboarding.di

import com.alekhin.beetea.onboarding.data.DataStoreRepository

interface MainModule {
    val provideDataStoreRepository: DataStoreRepository
}
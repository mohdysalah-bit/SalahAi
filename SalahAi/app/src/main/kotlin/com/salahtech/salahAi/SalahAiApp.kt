package com.salahtech.salahAi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SalahAiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize app-level configurations
    }
}

package com.zattoo.movies

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MoviesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setup()
    }

    private fun setup() {
        Timber.plant(Timber.DebugTree())
    }
}
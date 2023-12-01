package com.hartleyv.android.osrsget

import android.app.Application

class GeTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ItemRepository.initialize(this)
        WikiRepository.initalize(this)
    }
}
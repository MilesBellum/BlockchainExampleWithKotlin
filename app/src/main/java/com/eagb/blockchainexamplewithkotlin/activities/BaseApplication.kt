package com.eagb.blockchainexamplewithkotlin.activities

import android.app.Application
import com.google.android.material.color.DynamicColors

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Applies dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
package com.eagb.blockchainexamplewithkotlin.managers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class AppManager(val context: Context) {
    /**
     * Restarts the app.
     */
    fun restartApp() {
        val manager: PackageManager = context.packageManager
        val intent = manager.getLaunchIntentForPackage(context.packageName)
        if (intent != null) {
            val mainIntent = Intent.makeRestartActivityTask(intent.component)
            context.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
    }
}

package com.eagb.blockchainexamplewithkotlin.managers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class AppManager(val context: Context) {

    fun restartApp() {
        val manager: PackageManager = context.packageManager
        val intent = manager.getLaunchIntentForPackage(context.packageName)
        val mainIntent = Intent.makeRestartActivityTask(intent!!.component)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}

package com.eagb.blockchainexamplewithkotlin.activities

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.content.Intent.CATEGORY_LAUNCHER
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import com.eagb.blockchainexamplewithkotlin.managers.SharedPreferencesManager

class RestorePowShortcutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = SharedPreferencesManager(this)

        val intent = Intent(this, MainActivity::class.java)
        intent.action = ACTION_MAIN
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        intent.flags = FLAG_ACTIVITY_CLEAR_TOP
        intent.addCategory(CATEGORY_LAUNCHER)
        prefs.setPowValue(SharedPreferencesManager.DEFAULT_PROOF_OF_WORK)
        startActivity(intent)
    }
}

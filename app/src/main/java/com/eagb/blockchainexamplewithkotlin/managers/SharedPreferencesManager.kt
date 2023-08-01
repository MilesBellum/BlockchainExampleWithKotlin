package com.eagb.blockchainexamplewithkotlin.managers

import android.content.Context

class SharedPreferencesManager(context: Context) {

    companion object {
        const val PREFERENCES_DATA: String = "eagb.blockchainexamplewithkotlin"
        const val ENCRYPTION_STATUS: String = "encryption_status"
        const val DARK_THEME: String = "dark_theme"
        const val PROOF_OF_WORK: String = "proof_of_work"
        const val DEFAULT_PROOF_OF_WORK = 2
    }

    private val sharedPreference = context.getSharedPreferences(PREFERENCES_DATA, Context.MODE_PRIVATE)
    private var editor = sharedPreference.edit()

    fun setPowValue(powValue: Int) {
        editor.putInt(PROOF_OF_WORK, powValue)
        editor.commit()
        editor.apply()
    }

    fun getPowValue(): Int {
        return sharedPreference.getInt(PROOF_OF_WORK, DEFAULT_PROOF_OF_WORK)
    }

    fun setEncryptionStatus(isActivated: Boolean) {
        editor.putBoolean(ENCRYPTION_STATUS, isActivated)
        editor.commit()
        editor.apply()
    }

    fun getEncryptionStatus(): Boolean {
        return sharedPreference.getBoolean(ENCRYPTION_STATUS, false)
    }

    fun setDarkTheme(isActivated: Boolean) {
        editor.putBoolean(DARK_THEME, isActivated)
        editor.commit()
        editor.apply()
    }

    fun isDarkTheme(): Boolean {
        return sharedPreference.getBoolean(DARK_THEME, false)
    }
}

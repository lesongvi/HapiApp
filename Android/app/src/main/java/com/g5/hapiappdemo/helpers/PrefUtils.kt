package com.g5.hapiappdemo.helpers

import android.content.Context
import android.content.SharedPreferences

import android.preference.PreferenceManager


class PrefUtils(context: Context?) {
    private val mPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    var startedTime: Int
        get() = mPreferences.getInt(START_TIME, 0)
        set(startedTime) {
            val editor = mPreferences.edit()
            editor.putInt(START_TIME, startedTime)
            editor.apply()
        }
    var maxTime: Int
        get() = mPreferences.getInt(MAX_TIME, 0)
        set(startedTime) {
            val editor = mPreferences.edit()
            editor.putInt(MAX_TIME, startedTime)
            editor.apply()
        }

    companion object {
        private const val START_TIME = "Countdown_timer"
        private const val MAX_TIME = "Countdown_max"
    }

}
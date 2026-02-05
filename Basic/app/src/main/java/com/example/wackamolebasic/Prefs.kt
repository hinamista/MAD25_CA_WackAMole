package com.example.wackamolebasic

import android.content.Context

object Prefs {
    private const val FILE = "wack_a_mole_prefs"
    private const val KEY_HIGH_SCORE = "high_score"

    fun getHighScore(context: Context): Int {
        val sp = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        return sp.getInt(KEY_HIGH_SCORE, 0)
    }

    fun setHighScore(context: Context, value: Int) {
        val sp = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        sp.edit().putInt(KEY_HIGH_SCORE, value).apply()
    }
}

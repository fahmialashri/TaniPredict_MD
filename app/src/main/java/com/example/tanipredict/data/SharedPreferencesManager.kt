package com.example.tanipredict.data

import android.content.Context

object SharedPreferencesManager {

    private const val PREF_NAME = "TaniPredictPrefs"
    private const val TOKEN_KEY = "auth_token"

    // Fungsi untuk menyimpan token
    fun saveToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    // Fungsi untuk mengambil token
    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY, null) // Mengembalikan null jika token tidak ada
    }

    // Fungsi untuk menghapus token
    fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(TOKEN_KEY)
        editor.apply()
    }
}

package com.zzzombiecoder.svalker.state

import android.content.Context

class CodeStorage(context: Context) {

    private val preferences = context.getSharedPreferences("CodeStore", Context.MODE_PRIVATE)
    private val key = "CODE_KEY"

    fun hasCode(code: String): Boolean {
        val codeSet = preferences.getStringSet(key, emptySet())
        return codeSet.contains(code)
    }

    fun saveCode(code: String) {
        val codeSet = preferences.getStringSet(key, mutableSetOf())
        codeSet.add(code)
        preferences
                .edit()
                .putStringSet(key, codeSet)
                .apply()
    }
}
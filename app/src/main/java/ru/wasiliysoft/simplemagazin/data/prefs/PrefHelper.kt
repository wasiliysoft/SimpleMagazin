package ru.wasiliysoft.simplemagazin.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.wasiliysoft.simplemagazin.data.SimpleItem

class PrefHelper private constructor(context: Context) {
    companion object {
        private const val LOG_TAG = "PrefHelper"
        private const val PREF_FILE = "data_list"
        private const val PREF_JSON_LIST = "PREF_JSON_LIST"

        @Volatile private var instance: PrefHelper? = null

        fun initPreferences(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = PrefHelper(context.applicationContext)
                    }
                }
            }
        }

        fun getInstance(): PrefHelper = requireNotNull(instance) {
            "PrefHelper instance isn't create"
        }
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    private val gson by lazy { Gson() }

    var storedList: List<SimpleItem>
        get() {
            val json = prefs.getString(PREF_JSON_LIST, null) ?: return emptyList()
            return runCatching {
                gson.fromJson<List<SimpleItem>>(
                    json,
                    object : TypeToken<List<SimpleItem>>() {}.type
                ) ?: emptyList()
            }.getOrElse { emptyList() }
        }
        set(value) {
            prefs.edit { putString(PREF_JSON_LIST, gson.toJson(value)) }
        }
}
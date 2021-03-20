package ru.wasiliysoft.ssimplemagazin

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.wasiliysoft.ssimplemagazin.model.SimpleItem

class PrefHelper(ctx: Context) {
    private val prefFileName = ctx.packageName
    private val key_jsonPendingList = "JSON_PENDING_LIST"
    private val key_jsonSuccessList = "JSON_SUCCESS_LIST"
    private val sp = ctx.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    var pendingList: MutableList<SimpleItem>
        get() {
            val json = sp.getString(key_jsonPendingList, "").toString()
            val typeToken = (object : TypeToken<MutableList<SimpleItem>>() {}).type
            val items: MutableList<SimpleItem>? = Gson().fromJson(json, typeToken)
            return items ?: MutableList(0) { SimpleItem("") }
        }
        set(value) {
            val json = Gson().toJson(value)
            sp.edit().putString(key_jsonPendingList, json).apply()
        }

    var successList: MutableList<SimpleItem>
        get() {
            val json = sp.getString(key_jsonSuccessList, "").toString()
            val typeToken = (object : TypeToken<MutableList<SimpleItem>>() {}).type
            val items: MutableList<SimpleItem>? = Gson().fromJson(json, typeToken)
            return items ?: MutableList(0) { SimpleItem("") }
        }
        set(value) {
            val json = Gson().toJson(value)
            sp.edit().putString(key_jsonSuccessList, json).apply()
        }
}
package ru.wasiliysoft.ssimplemagazin

import android.content.Context
import ru.wasiliysoft.ssimplemagazin.model.SimpleItem

class PrefHelper(
    private val ctx: Context
) {
    private val prefFileName = ctx.packageName
    private val key_jsonItemList = "JSON_ITEM_LIST"
    val sp = ctx.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    var jsonItemList: String
        get() = sp.getString(key_jsonItemList, "").toString()
        set(value) = sp.edit().putString(key_jsonItemList, value).apply()
}
package ru.wasiliysoft.simplemagazin.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DAO private constructor(context: Context) {

    companion object {
        private var INSTANCE: DAO? = null
        fun getInstance(context: Context): DAO {
            if (INSTANCE == null) {
                INSTANCE = DAO(context)
            }
            return INSTANCE!!
        }

        private const val prefFileName = "data_list"
        private const val PREF_JSON_LIST = "PREF_JSON_LIST"
    }

    private val sp = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    private var list = mutableListOf<SimpleItem>()

    init {
        try {
            val json = sp.getString(PREF_JSON_LIST, "").toString()
            val typeToken = (object : TypeToken<MutableList<SimpleItem>>() {}).type
            list = Gson().fromJson(json, typeToken)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getList(): List<SimpleItem> = list

    private fun commit() {
        sp.edit().putString(PREF_JSON_LIST, Gson().toJson(list)).apply()
    }

    fun insert(simpleItem: SimpleItem) {
        list.add(simpleItem)
        commit()
    }

    fun toSuccess(id: String) {
        list.find { it.id == id }?.apply {
            isSuccess = true
            commit()
        }
    }

    fun toPending(id: String) {
        list.find { it.id == id }?.apply {
            isSuccess = false
            commit()
        }
    }

    fun delete(id: String) {
        list.find { it.id == id }?.let {
            list.remove(it)
            commit()
        }
    }
}
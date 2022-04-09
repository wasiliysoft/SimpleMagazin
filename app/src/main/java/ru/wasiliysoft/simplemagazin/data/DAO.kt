package ru.wasiliysoft.simplemagazin.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
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

    private val _list = getList().toMutableList()
    val list = MutableLiveData(_list)

    private fun notifyDataSetChanged() {
        list.value = _list
    }

    private fun getList(): List<SimpleItem> {
        val json = sp.getString(PREF_JSON_LIST, "").toString()
        val typeToken = (object : TypeToken<MutableList<SimpleItem>>() {}).type
        return try {
            Gson().fromJson(json, typeToken) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun commit(newList: List<SimpleItem>) {
        notifyDataSetChanged()
        sp.edit().putString(PREF_JSON_LIST, Gson().toJson(newList)).apply()
    }

    fun insert(simpleItem: SimpleItem) {
        _list.add(simpleItem)
        commit(_list)
    }

    fun delete(item: SimpleItem) {
        _list.remove(item)
        commit(_list)
    }

    fun update(item: SimpleItem) {
        val oldItem = _list.find { it.id == item.id } ?: return
        val pos = _list.indexOf(oldItem)
        if (pos > -1) {
            _list[pos] = item
            commit(_list)
        }
    }

    fun toPending(simpleItem: SimpleItem) {
        update(simpleItem.copy(isSuccess = false))
    }

    fun toSuccess(simpleItem: SimpleItem) {
        update(simpleItem.copy(isSuccess = true))
    }
}
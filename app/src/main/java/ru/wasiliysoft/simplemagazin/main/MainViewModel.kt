package ru.wasiliysoft.simplemagazin.main

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.wasiliysoft.simplemagazin.data.DAO
import ru.wasiliysoft.simplemagazin.data.SimpleItem

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private var _enterSelectMode = mutableStateOf(false)
    val isSelectMode = _enterSelectMode
    fun enterSelectMode() {
        _enterSelectMode.value = true
    }

    fun exitSelectMode() {
        _enterSelectMode.value = false
    }

    private val dao = DAO.getInstance(app.applicationContext)
    private val _list = MutableLiveData(dao.getList())
    val list: LiveData<List<SimpleItem>> = _list
    val pendingList = Transformations.map(_list) { list ->
        list.filter { !it.isSuccess }
    }
    val successList = Transformations.map(_list) { list ->
        list.filter { it.isSuccess }
    }

    fun addItem(simpleItem: SimpleItem) {
        dao.insert(simpleItem)
        _list.value = dao.getList()
    }

    fun delete(list: List<SimpleItem>) {
        list.forEach {
            dao.delete(it.id)
        }
        _list.value = dao.getList()
    }

    fun toPending(simpleItem: SimpleItem) {
        dao.toPending(simpleItem)
        _list.value = dao.getList()
    }

    fun toSuccess(simpleItem: SimpleItem) {
        dao.toSuccess(simpleItem)
        _list.value = dao.getList()
    }
}
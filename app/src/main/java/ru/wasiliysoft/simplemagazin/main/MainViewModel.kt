package ru.wasiliysoft.simplemagazin.main

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import ru.wasiliysoft.simplemagazin.data.DAO
import ru.wasiliysoft.simplemagazin.data.SimpleItem

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private var _enterSelectMode = mutableStateOf(false)
    val isSelectMode = _enterSelectMode

    var selectedItems = mutableStateListOf<SimpleItem>()
        private set

    fun enterSelectMode() {
        _enterSelectMode.value = true
    }

    fun exitSelectMode() {
        _enterSelectMode.value = false
    }

    private val dao = DAO.getInstance(app.applicationContext)
    val list = dao.list
    val pendingList = Transformations.map(list) { list -> list.filter { !it.isSuccess } }
    val successList = Transformations.map(list) { list -> list.filter { it.isSuccess } }


    fun addItem(simpleItem: SimpleItem) {
        dao.insert(simpleItem)
    }

    fun update(simpleItem: SimpleItem) {
        dao.update(simpleItem)
    }

    fun delete(list: List<SimpleItem>) {
        list.forEach {
            dao.delete(it)
        }
    }

    fun toPending(item: SimpleItem) {
        dao.toPending(item)
    }

    fun toSuccess(item: SimpleItem) {
        dao.toSuccess(item)
    }
}
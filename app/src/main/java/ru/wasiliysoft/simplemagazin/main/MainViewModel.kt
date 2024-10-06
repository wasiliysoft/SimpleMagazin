package ru.wasiliysoft.simplemagazin.main

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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

    fun deleteSelectedItems() {
        val selected = list.value?.filter { it.selected } ?: emptyList()
        viewModelScope.launch {
            dao.delete(selected)
            exitSelectMode()
        }
    }

    private val dao = DAO.getInstance(app.applicationContext)
    val list = dao.list
    val pendingList = list.map { it.filter { item -> !item.isSuccess } }
    val successList = list.map { it.filter { item -> item.isSuccess } }


    fun addItem(simpleItem: SimpleItem) = viewModelScope.launch {
        dao.insert(simpleItem)
    }

    fun update(simpleItem: SimpleItem) = viewModelScope.launch {
        dao.update(simpleItem)
    }

    fun toPending(item: SimpleItem) = viewModelScope.launch {
        dao.toPending(item)
    }

    fun toSuccess(item: SimpleItem) = viewModelScope.launch {
        dao.toSuccess(item)
    }
}
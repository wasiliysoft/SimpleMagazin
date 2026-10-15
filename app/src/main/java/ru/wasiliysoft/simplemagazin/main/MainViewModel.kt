package ru.wasiliysoft.simplemagazin.main

import android.app.Application
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.wasiliysoft.simplemagazin.R
import ru.wasiliysoft.simplemagazin.data.DAO
import ru.wasiliysoft.simplemagazin.data.SimpleItem

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private var _enterSelectMode = mutableStateOf(false)
    val isSelectionMode = _enterSelectMode

    val tabList = listOf(R.string.tab_pending, R.string.tab_success)

    fun enterSelectMode() {
        _enterSelectMode.value = true
    }

    fun exitSelectMode() {
        _enterSelectMode.value = false
    }

    fun deleteSelectedItems(pagerState: PagerState) {
        val selected = when (tabList[pagerState.currentPage]) {
            R.string.tab_pending -> pendingList.value?.filter { it.selected } ?: emptyList()
            R.string.tab_success -> successList.value?.filter { it.selected } ?: emptyList()
            else -> emptyList()
        }
        viewModelScope.launch {
            dao.delete(selected)
            exitSelectMode()
        }
    }


    private val dao = DAO.getInstance(app.applicationContext)
    val list = dao.list
    val pendingList = list.map { it.filter { item -> !item.isSuccess } }
    val successList = list.map { it.filter { item -> item.isSuccess } }

    fun getSelectedCount(pagerState: PagerState): LiveData<Int> {
        val selected = when (tabList[pagerState.currentPage]) {
            R.string.tab_pending -> pendingList.map { items -> items.filter { it.selected }.size }
            R.string.tab_success -> successList.map { items -> items.filter { it.selected }.size }
            else -> throw Exception("pagerState not identified")
        }
        return selected
    }

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
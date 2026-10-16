package ru.wasiliysoft.simplemagazin.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.wasiliysoft.simplemagazin.data.MainTab
import ru.wasiliysoft.simplemagazin.data.SimpleItem
import ru.wasiliysoft.simplemagazin.repo.LocalStorage

class MainViewModel : ViewModel() {

    private val repo = LocalStorage

    val pendingList: StateFlow<List<SimpleItem>> = repo.list
        .map { items -> items.filter { !it.isSuccess } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val successList: StateFlow<List<SimpleItem>> = repo.list
        .map { items -> items.filter { it.isSuccess } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectionMode = MutableStateFlow(false)
    val isSelectionMode: StateFlow<Boolean> = _selectionMode.asStateFlow()
    fun enterSelectMode() {
        _selectionMode.value = true
    }

    fun exitSelectMode() {
        _selectionMode.value = false
    }

    private val _activeTab = MutableStateFlow(MainTab.PENDING)
    fun onTabChanged(tab: MainTab) {
        _activeTab.value = tab
    }


    private val activeList: StateFlow<List<SimpleItem>> =
        combine(_activeTab, pendingList, successList) { tab, pending, success ->
            when (tab) {
                MainTab.PENDING -> pending
                MainTab.SUCCESS -> success
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedCount: StateFlow<Int> = activeList
        .map { items -> items.count { it.selected } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun deleteSelected() = viewModelScope.launch {
        val selected = activeList.value.filter { it.selected }
        if (selected.isEmpty()) return@launch
        repo.delete(selected)
        exitSelectMode()
    }

    fun addItem(item: SimpleItem) = viewModelScope.launch { repo.insert(item) }

    fun toggleSelected(item: SimpleItem) = viewModelScope.launch {
        repo.update(item.copy(selected = !item.selected))
    }

    fun toPending(item: SimpleItem) = viewModelScope.launch {
        repo.update(item.copy(isSuccess = false))
    }

    fun toSuccess(item: SimpleItem) = viewModelScope.launch {
        repo.update(item.copy(isSuccess = true))
    }
}
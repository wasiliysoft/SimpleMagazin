package ru.wasiliysoft.simplemagazin.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import ru.wasiliysoft.simplemagazin.data.SimpleItem
import ru.wasiliysoft.simplemagazin.data.prefs.PrefHelper

object LocalStorage {

    private val storage by lazy { PrefHelper.getInstance() }

    private val _list = MutableStateFlow(storage.storedList)
    val list: StateFlow<List<SimpleItem>> = _list.asStateFlow()

    private val mutex = Mutex()

    /**
     * Атомарно: копирует текущий список, применяет transform,
     * сохраняет в SP и публикует новый снимок.
     */
    private suspend fun mutate(transform: (List<SimpleItem>) -> List<SimpleItem>) {
        mutex.withLock {
            val updated = transform(_list.value)
            withContext(Dispatchers.IO) {
                storage.storedList = updated
            }
            _list.value = updated
        }
    }

    suspend fun insert(item: SimpleItem) = mutate { it + item }

    suspend fun delete(items: List<SimpleItem>) {
        val ids = items.mapTo(HashSet()) { it.id }
        mutate { list -> list.filterNot { it.id in ids } }
    }

    suspend fun update(item: SimpleItem) = mutate { list ->
        val index = list.indexOfFirst { it.id == item.id }
        if (index < 0) return@mutate list

        val old = list[index]
        if (old.isSuccess != item.isSuccess) {
            // перенос между списками — в конец
            list.toMutableList().apply {
                removeAt(index)
                add(item)
            }
        } else {
            list.toMutableList().apply { this[index] = item }
        }
    }

    suspend fun updateAll(items: List<SimpleItem>) = mutate { list ->
        val byId = items.associateBy { it.id }
        list.map { byId[it.id] ?: it }
    }
}
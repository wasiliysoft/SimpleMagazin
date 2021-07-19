package ru.wasiliysoft.simplemagazin.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.wasiliysoft.simplemagazin.data.DAO
import ru.wasiliysoft.simplemagazin.data.SimpleItem

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = DAO.getInstance(app.applicationContext)
    private val _list = MutableLiveData(dao.getList())
    val list: LiveData<List<SimpleItem>> = _list

    fun addItem(simpleItem: SimpleItem) {
        dao.insert(simpleItem)
        _list.value = dao.getList()
    }

    fun toPending(id: String) {
        dao.toPending(id)
        _list.value = dao.getList()
    }

    fun delete(list: List<SimpleItem>) {
        list.forEach {
            dao.delete(it.id)
        }
        _list.value = dao.getList()
    }

    fun toSuccess(id: String) {
        dao.toSuccess(id)
        _list.value = dao.getList()
    }
}
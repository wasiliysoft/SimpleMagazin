package ru.wasiliysoft.simplemagazin.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.wasiliysoft.simplemagazin.data.DAO
import ru.wasiliysoft.simplemagazin.data.SimpleItem

class DetailEditViewModel(app: Application) : AndroidViewModel(application = app) {
    private val repository = DAO.getInstance(app.applicationContext)
    fun addItem(simpleItem: SimpleItem) = viewModelScope.launch {
        repository.insert(simpleItem)
    }

    fun update(simpleItem: SimpleItem) = viewModelScope.launch {
        repository.update(simpleItem)
    }

}
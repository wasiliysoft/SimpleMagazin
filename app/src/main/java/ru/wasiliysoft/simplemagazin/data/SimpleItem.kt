package ru.wasiliysoft.simplemagazin.data

import java.util.*

data class SimpleItem(
    val title: String,
    val id: String = UUID.randomUUID().toString(),
    var selected: Boolean = false,
    var isSuccess: Boolean = false
)

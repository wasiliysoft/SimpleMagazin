package ru.wasiliysoft.simplemagazin.data

import java.util.*

data class SimpleItem(
    val title: String,
    val id: String = UUID.randomUUID().toString(),
    val selected: Boolean = false,
    val isSuccess: Boolean = false
)

package ru.wasiliysoft.ssimplemagazin.model

import java.util.*

data class SimpleItem(
    val title: String,
    val id: String = UUID.randomUUID().toString(),
    var selected: Boolean = false
)

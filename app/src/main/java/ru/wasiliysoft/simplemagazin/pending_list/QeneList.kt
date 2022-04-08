package ru.wasiliysoft.simplemagazin.pending_list

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun QueuneList(list: List<String>) {
    Column {
        list.forEach {
            Text(it)
        }
    }
}
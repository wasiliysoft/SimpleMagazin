package ru.wasiliysoft.simplemagazin.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SuccessesFragment(model: MainViewModel) {
    val items by model.successList.collectAsStateWithLifecycle()
    val isSelectionMode by model.isSelectionMode.collectAsStateWithLifecycle()

    ItemListScreen(
        items = items,
        isSelectionMode = isSelectionMode,
        onItemClick = model::toggleSelected,
        onItemLongClick = model::enterSelectMode,
        onItemDoubleClick = model::toPending,
    )
}

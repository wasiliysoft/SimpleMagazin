package ru.wasiliysoft.simplemagazin

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun topAppBar() {
    TopAppBar(title = { Text(text = "LayoutsCodelab") }, elevation = 0.dp)
}

@Composable
fun topAppBarActionMode(
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    TopAppBar(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        title = { Text(text = "LayoutsCodelab") },
        navigationIcon = {
            IconButton(onClick = onCancel) {
                Icon(Icons.Filled.Close, null)
            }
        },
        actions = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Done, null)
            }
        })
}

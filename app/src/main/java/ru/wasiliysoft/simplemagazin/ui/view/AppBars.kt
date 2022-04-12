package ru.wasiliysoft.simplemagazin.ui.view

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.wasiliysoft.simplemagazin.R

@Composable
fun topAppBar() {
    TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }, elevation = 0.dp)
}

@Composable
fun topAppBarActionMode(
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    TopAppBar(
        title = { Text("") },
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        navigationIcon = {
            IconButton(onClick = onCancel) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, null)
            }
        })
}

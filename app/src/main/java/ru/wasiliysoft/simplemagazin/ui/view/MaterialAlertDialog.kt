package ru.wasiliysoft.simplemagazin.ui.view

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun MaterialAlertDialog(
    text: String,
    positiveButtonText: String = "OK",
    title: String? = null,
    onDismissRequest: () -> Unit,
    onPositiveClick: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismissRequest,
        title = {
            if (!title.isNullOrBlank()) {
                Text(title)
            }
        },
        text = {
            Text(text)
        },
        confirmButton = {
            TextButton(onClick = onPositiveClick) {
                Text(positiveButtonText)
            }
        }
    )
}
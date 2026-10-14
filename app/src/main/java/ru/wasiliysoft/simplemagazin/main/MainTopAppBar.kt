package ru.wasiliysoft.simplemagazin.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.wasiliysoft.simplemagazin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    isSelectionMode: Boolean,
    onDismissSelectionMode: () -> Unit = {},
    onDeleteAction: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = if (isSelectionMode) "" else stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.primary
            )
        },

        navigationIcon = {
            if (isSelectionMode) {
                IconButton(onClick = onDismissSelectionMode) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(android.R.string.cancel)
                    )
                }
            }
        },

        actions = {
            AnimatedVisibility(
                visible = isSelectionMode,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row {
                    IconButton(onClick = onDeleteAction) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(R.string.action_delete)
                        )
                    }
                }
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isSelectionMode) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
    )
}
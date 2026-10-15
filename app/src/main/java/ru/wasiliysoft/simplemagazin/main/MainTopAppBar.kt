package ru.wasiliysoft.simplemagazin.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ru.wasiliysoft.simplemagazin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    isSelectionMode: Boolean,
    selectedCount: Int,
    onDismissSelectionMode: () -> Unit,
    onDeleteAction: () -> Unit,
    modifier: Modifier = Modifier,
    chromeContainerColor: Color,
    chromeContentColor: Color
) {
    Surface(color = chromeContainerColor) {

        TopAppBar(
            modifier = modifier,
            title = {

                if (isSelectionMode) {
                    Text(
                        text = pluralStringResource(
                            R.plurals.selected_count, selectedCount, selectedCount
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }


            },
            navigationIcon = {
                if (isSelectionMode) {
                    IconButton(onClick = onDismissSelectionMode) {
                        Icon(Icons.Default.Close, contentDescription = "Отменить выбор")
                    }
                }
            },
            actions = {
                if (isSelectionMode) {
                    IconButton(onClick = onDeleteAction) {
                        Icon(Icons.Outlined.DeleteOutline, contentDescription = "Удалить")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                navigationIconContentColor = chromeContentColor,
                titleContentColor = chromeContentColor,
                actionIconContentColor = chromeContentColor
            )
        )

    }
}
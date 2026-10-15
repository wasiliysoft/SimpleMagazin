package ru.wasiliysoft.simplemagazin.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.wasiliysoft.simplemagazin.R
import ru.wasiliysoft.simplemagazin.data.SimpleItem
import ru.wasiliysoft.simplemagazin.main.MainViewModel

@Composable
fun PendingFragment(model: MainViewModel) {
    val isSelectedMode = model.isSelectionMode.value
    val cardCombinedClickableBehavior = object : CardCombinedClickable {
        override fun onLongClick(item: SimpleItem) {
            if (!isSelectedMode) {
                model.enterSelectMode()
            }
        }

        override fun onDoubleClick(item: SimpleItem) {
            if (!isSelectedMode) {
                model.toSuccess(item)
            }
        }

        override fun onClick(item: SimpleItem) {
            if (isSelectedMode) {
                model.update(item.copy(selected = !item.selected))
            }
        }
    }
    val items by model.pendingList.observeAsState(initial = listOf())
    Column(modifier = Modifier.fillMaxSize()) {
        if (items.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.Checklist,
                title = stringResource(R.string.empty_pending_title),
                subtitle = stringResource(R.string.empty_pending_subtitle),
                modifier = Modifier.weight(1f)
            )
        } else {
            CardList(
                list = items,
                cardCombinedClickable = cardCombinedClickableBehavior,
                selectableMode = isSelectedMode,
                modifier = Modifier.weight(1f)
            )
        }
        ItemInput(onItemComplete = model::addItem)
    }
}

@Composable
private fun EmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun SuccessedFragment(model: MainViewModel) {
    val isSelectedMode = model.isSelectionMode.value

    val cardCombinedClickableBehavior = object : CardCombinedClickable {
        override fun onLongClick(item: SimpleItem) {
            if (!isSelectedMode) {
                model.enterSelectMode()
            }
        }

        override fun onDoubleClick(item: SimpleItem) {
            if (!isSelectedMode) {
                model.toPending(item)
            }
        }

        override fun onClick(item: SimpleItem) {
            if (isSelectedMode) {
                model.update(item.copy(selected = !item.selected))
            }
        }
    }
    val items by model.successList.observeAsState(initial = emptyList())
    CardList(
        list = items,
        cardCombinedClickable = cardCombinedClickableBehavior,
        selectableMode = isSelectedMode,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
fun ItemInput(onItemComplete: (item: SimpleItem) -> Unit) {
    var text by remember { mutableStateOf("") }
    val submit = {
        if (text.isNotBlank()) {
            onItemComplete(SimpleItem(text.trim()))
            text = ""
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .navigationBarsPadding(),  // важно при edge-to-edge
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(stringResource(R.string.new_item_hint)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
                modifier = Modifier.weight(1f)
            )
            FilledIconButton(
                onClick = submit,
                enabled = text.isNotBlank(),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "Добавить")
            }
        }
    }
}

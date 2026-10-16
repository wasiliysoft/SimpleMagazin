package ru.wasiliysoft.simplemagazin.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.wasiliysoft.simplemagazin.data.SimpleItem

interface CardCombinedClickable {
    fun onLongClick(item: SimpleItem)
    fun onDoubleClick(item: SimpleItem)
    fun onClick(item: SimpleItem)
}

@Composable
fun CardList(
    list: List<SimpleItem>,
    cardCombinedClickable: CardCombinedClickable,
    selectableMode: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    var previousCount by remember { mutableIntStateOf(list.size) }
    LaunchedEffect(list.size) {
        // Скроллим только если элементов стало БОЛЬШЕ
        if (list.size > previousCount) {
            scrollState.animateScrollToItem(list.size - 1)
        }
        // Обновляем счетчик для следующего раза
        previousCount = list.size
    }

    LazyColumn(
        state = scrollState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),   // плотнее
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    ) {
        items(count = list.size, key = { list[it].id }) { pos ->
            ItemCard(
                item = list[pos],
                cardCombinedClickable = cardCombinedClickable,
                selectableMode = selectableMode,
                modifier = Modifier.animateItem(
//                    fadeInSpec = null,
//                    fadeOutSpec = null,
//                    placementSpec = null
                )
            )
        }
    }
}

@Composable
private fun ItemCard(
    item: SimpleItem,
    cardCombinedClickable: CardCombinedClickable,
    selectableMode: Boolean,
    modifier: Modifier = Modifier
) {
    val isSelected = item.selected

    val containerColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surfaceContainerLow  // subtle surface, не белый
        },
        label = "SelectCardAnimation"
    )
    val contentColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    val clickModifier = if (selectableMode) {
        Modifier.selectable(selected = isSelected) { cardCombinedClickable.onClick(item) }
    } else {
        Modifier.combinedClickable(
            onClick = {},
            onDoubleClick = { cardCombinedClickable.onDoubleClick(item) },
            onLongClick = { cardCombinedClickable.onLongClick(item) }
        )
    }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .then(clickModifier),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .defaultMinSize(minHeight = 44.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { cardCombinedClickable.onDoubleClick(item) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (item.isSuccess) Icons.AutoMirrored.Outlined.Undo else Icons.Outlined.CheckCircle,
                    contentDescription = if (item.isSuccess) "Вернуть в активные" else "Отметить выполненным",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
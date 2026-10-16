package ru.wasiliysoft.simplemagazin.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.wasiliysoft.simplemagazin.data.SimpleItem


@Composable
fun ItemListScreen(
    items: List<SimpleItem>,
    isSelectionMode: Boolean,
    onItemClick: (SimpleItem) -> Unit,
    onItemLongClick: () -> Unit,
    onItemDoubleClick: (SimpleItem) -> Unit,
    emptyState: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
) {
    Column(Modifier.fillMaxSize()) {
        if (items.isEmpty() && emptyState != null) {
            Box(Modifier.weight(1f)) { emptyState() }
        } else {
            CardList(
                list = items,
                selectableMode = isSelectionMode,
                onItemClick = onItemClick,
                onItemLongClick = onItemLongClick,
                onItemDoubleClick = onItemDoubleClick,
                modifier = Modifier.weight(1f)
            )
        }
        bottomBar?.invoke()
    }
}


@Composable
private fun CardList(
    list: List<SimpleItem>,
    selectableMode: Boolean,
    onItemClick: (SimpleItem) -> Unit,
    onItemLongClick: () -> Unit,
    onItemDoubleClick: (SimpleItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    val previousCountHolder = remember { arrayOf(list.size) }

    LaunchedEffect(list.size) {
        val prev = previousCountHolder[0]
        if (list.size > prev) {
            // даём кадр, чтобы новый item попал в layout
            withFrameNanos { }
            scrollState.animateScrollToItem(list.size - 1)
        }
        previousCountHolder[0] = list.size
    }

    LazyColumn(
        state = scrollState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    ) {
        items(items = list, key = { it.id }) { item ->
            ItemCard(
                item = item,
                selectableMode = selectableMode,
                onClick = onItemClick,
                onLongClick = onItemLongClick,
                onDoubleClick = onItemDoubleClick,
                modifier = Modifier.animateItem(
                    fadeInSpec = tween(200),
                    fadeOutSpec = tween(200),
                    placementSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    )
                )
            )
        }
    }
}

@Composable
private fun ItemCard(
    item: SimpleItem,
    selectableMode: Boolean,
    onClick: (SimpleItem) -> Unit,
    onLongClick: () -> Unit,
    onDoubleClick: (SimpleItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (item.selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
        label = "SelectCardAnimation"
    )

    val contentColor by animateColorAsState(
        targetValue = if (item.selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        label = "SelectContentAnimation"
    )

    val clickModifier = if (selectableMode) {
        Modifier.selectable(
            selected = item.selected,
            onClick = { onClick(item) }
        )
    } else {
        Modifier.combinedClickable(
            onClick = {},
            onDoubleClick = { onDoubleClick(item) },
            onLongClick = onLongClick
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
                onClick = { onDoubleClick(item) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (item.isSuccess) {
                        Icons.AutoMirrored.Outlined.Undo
                    } else {
                        Icons.Outlined.CheckCircle
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

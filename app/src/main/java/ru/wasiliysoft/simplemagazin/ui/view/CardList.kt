package ru.wasiliysoft.simplemagazin.ui.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        state = scrollState, modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemCard(
    item: SimpleItem,
    cardCombinedClickable: CardCombinedClickable,
    selectableMode: Boolean,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(
        targetValue = if (item.selected) MaterialTheme.colorScheme.tertiaryContainer
        else CardDefaults.cardColors().containerColor, label = "SelectCardAnimation"
    )
    var m = modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 48.dp)
    m = if (selectableMode) {
        m.selectable(selected = item.selected) {
            println("onClick in selectable")
            cardCombinedClickable.onClick(item)
        }
    } else {
        m.combinedClickable(
            onClick = { println("onClick in combinedClickable") },
            onDoubleClick = { cardCombinedClickable.onDoubleClick(item) },
            onLongClick = { cardCombinedClickable.onLongClick(item) }
        )
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = m
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.title,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .weight(1.0f)
            )
            if (item.isSuccess) {
                IconButton(
                    onClick = { cardCombinedClickable.onDoubleClick(item) }) {
                    Icon(Icons.Filled.Restore, "Restore")
                }
            } else {
                IconButton(
                    onClick = { cardCombinedClickable.onDoubleClick(item) }) {
                    Icon(Icons.Filled.Done, "Done")
                }
            }
        }

    }
}

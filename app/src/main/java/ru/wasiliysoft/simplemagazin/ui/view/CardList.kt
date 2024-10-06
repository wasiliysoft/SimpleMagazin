package ru.wasiliysoft.simplemagazin.ui.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    selectableMode: Boolean
) {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(count = list.size, key = { list[it].id }) {
            val pos = it
            ItemCard(
                item = list[pos],
                cardCombinedClickable = cardCombinedClickable,
                selectableMode = selectableMode,
                modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
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
        .padding(8.dp)
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
        Text(item.title, modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
    }
}

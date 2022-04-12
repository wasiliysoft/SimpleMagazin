package ru.wasiliysoft.simplemagazin.ui.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.wasiliysoft.simplemagazin.data.SimpleItem

interface CardCombinedClickable {
    fun onLongClick(item: SimpleItem)
    fun onDoubleClick(item: SimpleItem)
    fun onClick(item: SimpleItem)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
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
            itemCard(
                item = list[pos],
                cardCombinedClickable = cardCombinedClickable,
                selectableMode = selectableMode,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun itemCard(
    item: SimpleItem,
    cardCombinedClickable: CardCombinedClickable,
    selectableMode: Boolean,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(
        targetValue = if (item.selected) MaterialTheme.colors.primaryVariant
        else MaterialTheme.colors.background
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
    Card(backgroundColor = color, modifier = m) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Text(item.title, modifier = Modifier.padding(8.dp))
        }
    }
}

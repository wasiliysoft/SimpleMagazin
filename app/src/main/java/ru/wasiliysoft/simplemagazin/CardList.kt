package ru.wasiliysoft.simplemagazin

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardList(
    list: List<SimpleItem>,
    cardCombinedClickable: CardCombinedClickable,
    selectableMode: Boolean

) {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(count = list.size) {
            val pos = it
            Surface(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {
                itemCard(
                    item = list[pos],
                    cardCombinedClickable = cardCombinedClickable,
                    selectableMode = selectableMode
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun itemCard(
    item: SimpleItem,
    cardCombinedClickable: CardCombinedClickable,
    selectableMode: Boolean
) {
    val color by animateColorAsState(
        targetValue = if (item.selected) MaterialTheme.colors.primaryVariant
        else MaterialTheme.colors.background
    )
    var modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .defaultMinSize(minHeight = 48.dp)
    modifier = if (selectableMode) {
        modifier.selectable(selected = item.selected) {
            println("onClick in selectable")
            cardCombinedClickable.onClick(item)
        }
    } else {
        modifier.combinedClickable(
            onClick = { println("onClick in combinedClickable") },
            onDoubleClick = { cardCombinedClickable.onDoubleClick(item) },
            onLongClick = { cardCombinedClickable.onLongClick(item) }
        )
    }
    Card(backgroundColor = color, modifier = modifier) {
        Text(item.title, modifier = Modifier.padding(8.dp))
    }
}

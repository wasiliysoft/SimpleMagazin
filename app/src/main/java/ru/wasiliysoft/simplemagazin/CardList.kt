package ru.wasiliysoft.simplemagazin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
    cardCombinedClickable: CardCombinedClickable
) {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(count = list.size) {
            val pos = it
            Surface(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {
                itemCard(item = list[pos], cardCombinedClickable = cardCombinedClickable)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun itemCard(item: SimpleItem, cardCombinedClickable: CardCombinedClickable) {
    Card(backgroundColor = if (item.selected) MaterialTheme.colors.primaryVariant
    else MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .defaultMinSize(minHeight = 48.dp)
            .combinedClickable(
                onClick = { cardCombinedClickable.onClick(item) },
                onDoubleClick = { cardCombinedClickable.onDoubleClick(item) },
                onLongClick = { cardCombinedClickable.onLongClick(item) }
            )
    ) {
        Text(item.title, modifier = Modifier.padding(8.dp))
    }
}

package ru.wasiliysoft.simplemagazin.pending_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.wasiliysoft.simplemagazin.data.SimpleItem


@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
fun QueuneListPreview() {
    val demoList = listOf(
        SimpleItem("123", "1"),
        SimpleItem("456", "1"),
        SimpleItem("123", "1"),
    )
    QueuneList(demoList, {}, {})
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QueuneList(
    list: List<SimpleItem>,
    onClick: (pos: Int) -> Unit,
    onLongClick: () -> Unit,
) {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(count = list.size) {
            val pos = it
            Surface(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {
                itemCard(
                    item = list[pos],
                    onClick = { onClick(pos) },
                    onLongClick = onLongClick
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun itemCard(item: SimpleItem, onClick: () -> Unit, onLongClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .defaultMinSize(minHeight = 48.dp)
            .combinedClickable(
                onClick = {},
                onDoubleClick = { onClick() },
                onLongClick = { onLongClick() }
            )
    ) {
        Text(item.title, modifier = Modifier.padding(8.dp))
    }
}

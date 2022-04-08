package ru.wasiliysoft.simplemagazin.pending_list

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import ru.wasiliysoft.simplemagazin.data.SimpleItem


@Composable
fun QueuneList(list: List<SimpleItem>, onClick: (pos: Int) -> Unit) {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(count = list.size) {
            val pos = it
            Card(modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onPress = { /* Called when the gesture starts */ },
                    onDoubleTap = { onClick(pos) },
                    onLongPress = { /* Called on Long Press */ },
                    onTap = { /* Called on Tap */ }
                )
            }) {
                Text(list[pos].title)
            }
        }
    }
}
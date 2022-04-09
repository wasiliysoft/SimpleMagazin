package ru.wasiliysoft.simplemagazin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.wasiliysoft.simplemagazin.data.SimpleItem
import ru.wasiliysoft.simplemagazin.main.MainViewModel
import java.util.*

@Composable
fun pendingFragment(model: MainViewModel) {
    val cardCombinedClickableBehavior = object : CardCombinedClickable {
        val isSelectedMode = model.isSelectMode.value
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
    Column(modifier = Modifier.fillMaxSize()) {
        val items by model.pendingList.observeAsState(initial = listOf())
        Surface(modifier = Modifier.weight(1f)) {
            CardList(list = items, cardCombinedClickable = cardCombinedClickableBehavior)
        }
        InputField { model.addItem(SimpleItem(it, UUID.randomUUID().toString())) }
    }
}

@Composable
fun succesedFragment(model: MainViewModel) {
    val cardCombinedClickableBehavior = object : CardCombinedClickable {
        val isSelectedMode = model.isSelectMode.value
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
    val items by model.successList.observeAsState(initial = listOf())
    Surface(modifier = Modifier.fillMaxSize()) {
        CardList(list = items, cardCombinedClickable = cardCombinedClickableBehavior)
    }
}


@Composable
fun InputField(onSend: (text: String) -> Unit) {
    var input by remember { mutableStateOf("") }
    Row {
        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.weight(1f)
        )
        OutlinedButton(onClick = {
            onSend(input)
            input = ""
        }) {
            Text("OK")
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
fun InputFieldPreview() {
    InputField() {}
}
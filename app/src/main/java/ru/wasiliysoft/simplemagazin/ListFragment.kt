package ru.wasiliysoft.simplemagazin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.wasiliysoft.simplemagazin.data.SimpleItem
import ru.wasiliysoft.simplemagazin.main.MainViewModel
import ru.wasiliysoft.simplemagazin.pending_list.QueuneList
import java.util.*

@Composable
fun pendingFragment(model: MainViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val items: List<SimpleItem> by model.pendingList.observeAsState(listOf())
        Surface(modifier = Modifier.weight(1f)) {
            QueuneList(
                list = items,
                onClick = { pos -> model.toSuccess(items[pos]) },
                onLongClick = { model.enterSelectMode() })
        }
        InputField { model.addItem(SimpleItem(it, UUID.randomUUID().toString())) }
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

@Composable
fun succesedFragment(model: MainViewModel) {
    val items: List<SimpleItem> by model.successList.observeAsState(listOf())
    Surface(modifier = Modifier.fillMaxSize()) {
        QueuneList(
            list = items,
            onClick = { model.toPending(items[it]) },
            onLongClick = { model.enterSelectMode() })
    }
}
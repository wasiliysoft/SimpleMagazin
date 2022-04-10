package ru.wasiliysoft.simplemagazin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.wasiliysoft.simplemagazin.data.SimpleItem
import ru.wasiliysoft.simplemagazin.main.MainViewModel

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
            CardList(
                list = items,
                cardCombinedClickable = cardCombinedClickableBehavior,
                selectableMode = model.isSelectMode.value
            )
        }
        ItemInput(onItemComplete = model::addItem)
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
        CardList(
            list = items,
            cardCombinedClickable = cardCombinedClickableBehavior,
            selectableMode = model.isSelectMode.value
        )
    }
}

@Composable
fun ItemInput(onItemComplete: (item: SimpleItem) -> Unit) {
    val (text, setText) = remember { mutableStateOf("") }
    val submit = {
        onItemComplete(SimpleItem(text))
        setText("")
    }
    Row {
        InputText(
            text = text, onTextChange = setText, modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            onImeAction = submit
        )
        AddItemButton(
            onClick = submit,
            text = "OK",
            enabled = text.isNotBlank(),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = text,
        onValueChange = onTextChange,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide()
        }),
        modifier = modifier
    )
}

@Composable
fun AddItemButton(onClick: () -> Unit, text: String, enabled: Boolean, modifier: Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) { Text(text) }
}
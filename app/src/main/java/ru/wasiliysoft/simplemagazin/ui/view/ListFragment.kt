package ru.wasiliysoft.simplemagazin.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.wasiliysoft.simplemagazin.R
import ru.wasiliysoft.simplemagazin.data.SimpleItem
import ru.wasiliysoft.simplemagazin.main.MainViewModel

@Composable
fun pendingFragment(model: MainViewModel) {
    val isSelectedMode = model.isSelectMode.value
    val cardCombinedClickableBehavior = object : CardCombinedClickable {
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
    val items by model.pendingList.observeAsState(initial = listOf())
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1.0f)) {
            CardList(
                list = items,
                cardCombinedClickable = cardCombinedClickableBehavior,
                selectableMode = isSelectedMode,
            )
        }
        Surface(elevation = 4.dp) {
            ItemInput(onItemComplete = model::addItem)
        }
    }
}


@Composable
fun succesedFragment(model: MainViewModel) {
    val isSelectedMode = model.isSelectMode.value

    val cardCombinedClickableBehavior = object : CardCombinedClickable {
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
    Column() {
        Row(Modifier.weight(1.0f)) {
            CardList(
                list = items,
                cardCombinedClickable = cardCombinedClickableBehavior,
                selectableMode = isSelectedMode
            )
        }
    }
}

@Composable
fun ItemInput(onItemComplete: (item: SimpleItem) -> Unit) {
    val (text, setText) = remember { mutableStateOf("") }
    val submit = {
        onItemComplete(SimpleItem(text))
        setText("")
    }
    Row(Modifier.padding(8.dp)) {
        InputText(
            text = text,
            onTextChange = setText,
            modifier = Modifier
                .weight(1.0f)
                .padding(4.dp),
            onImeAction = submit
        )
        TextButton(
            onClick = submit,
            modifier = Modifier.align(Alignment.CenterVertically),
            enabled = text.isNotBlank(),
            content = { Text(text = "OK") }
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
        placeholder = { Text(stringResource(id = R.string.new_item_hint)) },
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
    )
}
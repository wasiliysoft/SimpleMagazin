package ru.wasiliysoft.simplemagazin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.wasiliysoft.simplemagazin.data.SimpleItem
import ru.wasiliysoft.simplemagazin.main.MainViewModel
import ru.wasiliysoft.simplemagazin.pending_list.QueuneList
import ru.wasiliysoft.simplemagazin.ui.theme.SimpleMagazinTheme
import java.util.*

class MainActivityCompose : ComponentActivity() {
    private val vm: MainViewModel by lazy {
        val fa = ViewModelProvider.AndroidViewModelFactory(this.application)
        ViewModelProvider(this, fa).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleMagazinTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "LayoutsCodelab")
                            }
                        )
                    },
                    content = { CombinedTab(vm) },
                )
            }
        }
    }


    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun CombinedTab(model: MainViewModel) {
        val tabData = listOf(
            "Купить" to Icons.Filled.ShoppingCart,
            "Куплено" to Icons.Filled.Done
        )
        val pagerState = rememberPagerState(0)

        Column {
            TabsSimpleMagazin(tabData, pagerState)
            HorizontalPager(
                count = tabData.size,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { index ->
                println(index)
                when (index) {
                    0 -> pendingFragment(model)
                    else -> succesedFragment(model)
                }
            }
        }
    }
}

@Composable
fun pendingFragment(model: MainViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val items: List<SimpleItem> by model.pendingList.observeAsState(listOf())
        Surface(modifier = Modifier.weight(1f)) {
            QueuneList(items) {
                model.toSuccess(items[it])
            }
        }
        InputField { model.addItem(SimpleItem(it, UUID.randomUUID().toString())) }
    }
}

@Composable
fun succesedFragment(model: MainViewModel) {
    val items: List<SimpleItem> by model.successList.observeAsState(listOf())
    Surface(modifier = Modifier.fillMaxSize()) {
        QueuneList(items) {
            model.toPending(items[it])
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsSimpleMagazin(tabData: List<Pair<String, ImageVector>>, pagerState: PagerState) {
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    TabRow(selectedTabIndex = tabIndex) {
        tabData.forEachIndexed { index, pair ->
            Tab(
                selected = tabIndex == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(index)
                    }
                },
                text = { Text(text = pair.first) },
//                icon = { Icon(imageVector = pair.second, contentDescription = null) }
            )
        }
    }
}

@Composable
fun InputField(onSend: (text: String) -> Unit) {
    var input by remember { mutableStateOf("") }
    Row {
        TextField(value = input, onValueChange = { input = it })
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

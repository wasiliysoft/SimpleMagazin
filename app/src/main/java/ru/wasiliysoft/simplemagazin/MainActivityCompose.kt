package ru.wasiliysoft.simplemagazin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.wasiliysoft.simplemagazin.ui.theme.SimpleMagazinTheme

class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleMagazinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CombinedTab()
                }

            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CombinedTab() {
    val tabData = listOf(
        "Купить" to Icons.Filled.ShoppingCart,
        "Куплено" to Icons.Filled.Done,
    )
    val pagerState = rememberPagerState(0)
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    Column {
        TabRow(selectedTabIndex = tabIndex) {
            tabData.forEachIndexed { index, pair ->
                Tab(selected = tabIndex == index, onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }, text = { Text(text = pair.first) },
                    icon = { Icon(imageVector = pair.second, contentDescription = null) })
            }
        }
        HorizontalPager(
            count = tabData.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { index ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = tabData[index].first)
                InputField()
            }
        }
    }
}


@Composable
fun InputField() {
    val input = remember { mutableStateOf("") }
    TextField(value = input.value, onValueChange = { input.value = it })
}
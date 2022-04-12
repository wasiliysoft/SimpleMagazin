package ru.wasiliysoft.simplemagazin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.wasiliysoft.simplemagazin.main.MainViewModel
import ru.wasiliysoft.simplemagazin.ui.theme.SimpleMagazinTheme

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
                        if (vm.isSelectMode.value) topAppBarActionMode(
                            onCancel = { vm.exitSelectMode() },
                            onDelete = { vm.deleteSelectedItems() }
                        ) else topAppBar()
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
            ) { tabIndex ->
                println(tabIndex)
                when (tabIndex) {
                    0 -> pendingFragment(model)
                    else -> succesedFragment(model)
                }
            }
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


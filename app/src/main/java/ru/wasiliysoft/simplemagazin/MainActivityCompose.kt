package ru.wasiliysoft.simplemagazin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
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
                var confirmDialogIsShow by remember { mutableStateOf(false) }
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        if (vm.isSelectMode.value) topAppBarActionMode(
                            onCancel = { vm.exitSelectMode() },
                            onDelete = { confirmDialogIsShow = true }
                        ) else topAppBar()
                    },
                    content = { CombinedTab(vm) },
                )
                if (confirmDialogIsShow) {
                    val onDismissRequest = { confirmDialogIsShow = false }
                    val onConfirm = {
                        vm.deleteSelectedItems()
                        onDismissRequest()
                    }
                    ConfirmDeleteDialog(
                        onDismissRequest = onDismissRequest,
                        selectedItemsCount = vm.list.value?.filter { it.selected }?.size ?: 0,
                        onConfirm = onConfirm
                    )
                }
            }
        }
    }


    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun CombinedTab(model: MainViewModel) {
        val tabData = listOf(
            stringResource(id = R.string.tab_pending).uppercase() to Icons.Filled.ShoppingCart,
            stringResource(id = R.string.tab_success).uppercase() to Icons.Filled.Done
        )
        val pagerState = rememberPagerState(0)
        Column {
            TabsSimpleMagazin(tabData, pagerState)
            Surface(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    count = tabData.size,
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { tabIndex ->
                    when (tabData[tabIndex].first.lowercase()) {
                        stringResource(id = R.string.tab_pending).lowercase() -> pendingFragment(
                            model
                        )
                        stringResource(id = R.string.tab_success).lowercase() -> succesedFragment(
                            model
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(
    onDismissRequest: () -> Unit,
    selectedItemsCount: Int,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Confirm") },
        text = { Text("Delete $selectedItemsCount selected items?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("DELETE")
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsSimpleMagazin(tabData: List<Pair<String, ImageVector>>, pagerState: PagerState) {
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    TabRow(selectedTabIndex = tabIndex, backgroundColor = MaterialTheme.colors.surface) {
        tabData.forEachIndexed { index, pair ->
            Tab(
                selected = tabIndex == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = { Text(text = pair.first) },

//                icon = { Icon(imageVector = pair.second, contentDescription = null) }
            )
        }
    }
}


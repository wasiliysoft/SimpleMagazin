package ru.wasiliysoft.simplemagazin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.wasiliysoft.simplemagazin.main.MainViewModel
import ru.wasiliysoft.simplemagazin.ui.theme.SimpleMagazinTheme
import ru.wasiliysoft.simplemagazin.ui.view.pendingFragment
import ru.wasiliysoft.simplemagazin.ui.view.succesedFragment
import ru.wasiliysoft.simplemagazin.ui.view.topAppBar
import ru.wasiliysoft.simplemagazin.ui.view.topAppBarActionMode

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
            stringResource(id = R.string.tab_pending),
            stringResource(id = R.string.tab_success)
        ).map { it.uppercase() }

        val pagerState = rememberPagerState(0)
        Column {
            TabBar(tabTitles = tabData, pagerState = pagerState)
            Surface(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    count = tabData.size,
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { tabIndex ->
                    when (tabData[tabIndex].uppercase()) {
                        stringResource(id = R.string.tab_pending).uppercase() -> pendingFragment(
                            model
                        )
                        stringResource(id = R.string.tab_success).uppercase() -> succesedFragment(
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
fun TabBar(
    tabTitles: List<String>,
    pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()
    val activeTabIndex = pagerState.currentPage
    TabRow(selectedTabIndex = activeTabIndex, backgroundColor = MaterialTheme.colors.surface) {
        tabTitles.forEachIndexed { index, tabTitle ->
            Tab(
                selected = activeTabIndex == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = { Text(text = tabTitle) }
            )
        }
    }
}


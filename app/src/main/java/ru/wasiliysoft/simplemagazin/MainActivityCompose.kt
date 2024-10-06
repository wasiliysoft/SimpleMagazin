package ru.wasiliysoft.simplemagazin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import ru.wasiliysoft.simplemagazin.main.MainViewModel
import ru.wasiliysoft.simplemagazin.ui.theme.AppTheme
import ru.wasiliysoft.simplemagazin.ui.view.PendingFragment
import ru.wasiliysoft.simplemagazin.ui.view.SuccessedFragment
import ru.wasiliysoft.simplemagazin.ui.view.TopAppBar
import ru.wasiliysoft.simplemagazin.ui.view.TopAppBarActionMode

class MainActivityCompose : ComponentActivity() {
    private val vm: MainViewModel by lazy {
        val fa = ViewModelProvider.AndroidViewModelFactory(this.application)
        ViewModelProvider(this, fa).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                var confirmDialogIsShow by remember { mutableStateOf(false) }
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        if (vm.isSelectMode.value) TopAppBarActionMode(
                            onCancel = { vm.exitSelectMode() },
                            onDelete = { confirmDialogIsShow = true }
                        ) else TopAppBar()
                    },
                    content = {
                        CombinedTab(vm, modifier = Modifier.padding(it))
                    },
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


    @Composable
    fun CombinedTab(model: MainViewModel, modifier: Modifier = Modifier) {
        val tabData = listOf(
            stringResource(id = R.string.tab_pending),
            stringResource(id = R.string.tab_success)
        ).map { it.uppercase() }

        val pagerState = rememberPagerState(pageCount = { tabData.size })
        Column(modifier = modifier) {
            TabBar(tabTitles = tabData, pagerState = pagerState)
            Surface(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { tabIndex ->
                    when (tabData[tabIndex].uppercase()) {
                        stringResource(id = R.string.tab_pending).uppercase() -> PendingFragment(
                            model
                        )

                        stringResource(id = R.string.tab_success).uppercase() -> SuccessedFragment(
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

@Composable
fun TabBar(
    tabTitles: List<String>,
    pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()
    val activeTabIndex = pagerState.currentPage
    TabRow(selectedTabIndex = activeTabIndex, containerColor = MaterialTheme.colorScheme.surface) {
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


package ru.wasiliysoft.simplemagazin.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.wasiliysoft.simplemagazin.R
import ru.wasiliysoft.simplemagazin.edit.DetailEditActivity
import ru.wasiliysoft.simplemagazin.ui.theme.AppTheme
import ru.wasiliysoft.simplemagazin.ui.view.PendingFragment
import ru.wasiliysoft.simplemagazin.ui.view.SuccessedFragment

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by lazy {
        val fa = ViewModelProvider.AndroidViewModelFactory(this.application)
        ViewModelProvider(this, fa).get(MainViewModel::class.java)
    }

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val isSelectionMode by remember { vm.isSelectionMode }

            val tabData = vm.tabList.map { resId -> stringResource(resId).uppercase() }
            val pagerState = rememberPagerState(pageCount = { tabData.size })

            BackHandler(enabled = isSelectionMode) { vm.exitSelectMode() }
            BackHandler(enabled = pagerState.currentPage != 0) {
                lifecycleScope.launch { pagerState.scrollToPage(0) }
            }

            AppTheme {
                var confirmDialogIsShow by remember { mutableStateOf(false) }
                Scaffold(
                    topBar = { StatusBarPlaceholder(isSelectionMode) }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues)
                            .fillMaxSize()
                            .imePadding()
                    ) {
                        // Анимированный TopBar
                        val isKeyboardVisible = WindowInsets.isImeVisible
                        AnimatedVisibility(
                            visible = !isKeyboardVisible,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column {
                                MainTopAppBar(
                                    isSelectionMode = isSelectionMode,
                                    onDismissSelectionMode = vm::exitSelectMode,
                                    onDeleteAction = { confirmDialogIsShow = true }
                                )
                                TabBar(
                                    tabTitles = tabData,
                                    pagerState = pagerState,
                                    isSelectionMode = isSelectionMode
                                )
                            }
                        }

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.weight(1f)
                        ) { tabIndex ->
                            when (tabData[tabIndex].uppercase()) {
                                stringResource(id = R.string.tab_pending).uppercase() -> {
                                    PendingFragment(vm)
                                }

                                stringResource(id = R.string.tab_success).uppercase() -> {
                                    SuccessedFragment(vm)
                                }
                            }
                        }
                    }

                    if (confirmDialogIsShow) {
                        val onDismissRequest = { confirmDialogIsShow = false }
                        val onConfirm = {
                            vm.deleteSelectedItems(pagerState)
                            onDismissRequest()
                        }
                        ConfirmDeleteDialog(
                            onDismissRequest = onDismissRequest,
                            onConfirm = onConfirm
                        )
                    }
                }
            }
        }
    }


    private fun launchEditActivity() {
        val i = Intent(this, DetailEditActivity::class.java)
        startActivity(i)
    }
}

@Composable
private fun StatusBarPlaceholder(isSelectionMode: Boolean) {
    val statusBarColor = when (isSelectionMode) {
        true -> MaterialTheme.colorScheme.surfaceVariant
        false -> MaterialTheme.colorScheme.primaryContainer
    }
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(statusBarColor)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {}
}

@Composable
private fun ConfirmDeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.confirmation)) },
        text = { Text(stringResource(R.string.delete_selected_items_question)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(android.R.string.ok))
            }
        }
    )
}

@Composable
private fun TabBar(
    tabTitles: List<String>,
    pagerState: PagerState,
    isSelectionMode: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val activeTabIndex = pagerState.currentPage
    TabRow(
        selectedTabIndex = activeTabIndex,
        containerColor = (if (isSelectionMode) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer),
        contentColor = (if (isSelectionMode) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
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


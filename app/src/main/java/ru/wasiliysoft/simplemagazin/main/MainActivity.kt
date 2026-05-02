package ru.wasiliysoft.simplemagazin.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import ru.wasiliysoft.simplemagazin.R
import ru.wasiliysoft.simplemagazin.edit.DetailEditActivity
import ru.wasiliysoft.simplemagazin.ui.theme.AppTheme
import ru.wasiliysoft.simplemagazin.ui.view.PendingFragment
import ru.wasiliysoft.simplemagazin.ui.view.SuccessedFragment
import ru.wasiliysoft.simplemagazin.ui.view.TopAppBar
import ru.wasiliysoft.simplemagazin.ui.view.TopAppBarActionMode

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by lazy {
        val fa = ViewModelProvider.AndroidViewModelFactory(this.application)
        ViewModelProvider(this, fa).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                var confirmDialogIsShow by remember { mutableStateOf(false) }
                val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()    // Отступ сверху (часы)
                            .navigationBarsPadding() // Отступ снизу (полоска жестов)
                            .imePadding()           // Сама клавиатура
                    ) {
                        val tabData =
                            vm.tabList.map { resId -> stringResource(resId).uppercase() }
                        val pagerState = rememberPagerState(pageCount = { tabData.size })

                        // 1. Анимированный TopBar
                        AnimatedVisibility(
                            visible = !isKeyboardVisible,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column {
                                if (vm.isSelectMode.value) TopAppBarActionMode(
                                    onCancel = { vm.exitSelectMode() },
                                    onDelete = { confirmDialogIsShow = true }
                                ) else TopAppBar(::launchEditActivity)
                                TabBar(tabTitles = tabData, pagerState = pagerState)
                            }
                        }

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.weight(1f)
                        ) { tabIndex ->
                            when (tabData[tabIndex].uppercase()) {
                                stringResource(id = R.string.tab_pending)
                                    .uppercase() -> PendingFragment(vm)

                                stringResource(id = R.string.tab_success)
                                    .uppercase() -> SuccessedFragment(vm)
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
    }

    private fun launchEditActivity() {
        val i = Intent(this, DetailEditActivity::class.java)
        startActivity(i)
    }
}


@Composable
fun ConfirmDeleteDialog(
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
fun TabBar(
    tabTitles: List<String>,
    pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()
    val activeTabIndex = pagerState.currentPage
    TabRow(
        selectedTabIndex = activeTabIndex,
        containerColor = MaterialTheme.colorScheme.primaryContainer
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


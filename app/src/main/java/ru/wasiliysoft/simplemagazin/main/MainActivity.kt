package ru.wasiliysoft.simplemagazin.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        ViewModelProvider(this, fa)[MainViewModel::class.java]
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
                val chromeContainerColor by animateColorAsState(
                    targetValue = if (isSelectionMode) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing),
                    label = "ChromeContainer"
                )
                val chromeContentColor by animateColorAsState(
                    targetValue = if (isSelectionMode) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    animationSpec = tween(durationMillis = 200, easing = LinearEasing),
                    label = "ChromeContent"
                )
                var confirmDialogIsShow by remember { mutableStateOf(false) }
                Scaffold(topBar = { StatusBarPlaceholder(chromeContainerColor) }
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
                                    chromeContainerColor = chromeContainerColor,
                                    chromeContentColor = chromeContentColor,
                                    onDismissSelectionMode = vm::exitSelectMode,
                                    selectedCount = vm.getSelectedCount(pagerState).observeAsState(0).value,
                                    onDeleteAction = { confirmDialogIsShow = true }
                                )
                                TabBar(
                                    tabTitles = tabData,
                                    pagerState = pagerState,
                                    chromeContainerColor = chromeContainerColor,
                                    chromeContentColor = chromeContentColor,
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
                        ConfirmDeleteSheet(
                            onConfirm = onConfirm,
                            onDismissRequest = onDismissRequest,
                            selectedCount = vm.getSelectedCount(pagerState).observeAsState(0).value,
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
private fun StatusBarPlaceholder(color: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {}
}

@Composable
private fun TabBar(
    tabTitles: List<String>,
    pagerState: PagerState,
    chromeContainerColor: Color,
    chromeContentColor: Color
) {
    val coroutineScope = rememberCoroutineScope()
    val activeTabIndex = pagerState.currentPage
    SecondaryTabRow(
        selectedTabIndex = activeTabIndex,
        containerColor = chromeContainerColor,
        contentColor = chromeContentColor
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
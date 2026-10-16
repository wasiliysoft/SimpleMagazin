package ru.wasiliysoft.simplemagazin.data

import androidx.annotation.StringRes
import ru.wasiliysoft.simplemagazin.R

enum class MainTab(@StringRes val titleRes: Int) {
    PENDING(R.string.tab_pending),
    SUCCESS(R.string.tab_success);

    companion object {
        fun fromIndex(index: Int): MainTab = entries.getOrElse(index) { PENDING }
    }
}
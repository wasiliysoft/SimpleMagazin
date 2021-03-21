package ru.wasiliysoft.ssimplemagazin

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes


interface ActionModeCallback {
    fun onActionItemClick(item: MenuItem)
    fun onDestroyActionMode()
}


class PrimaryActionModeCallback : ActionMode.Callback {
    var mode: ActionMode? = null
    var callback: ActionModeCallback? = null


    @MenuRes
    private var menuResId = 0
    private var title: String? = null
    private var subtitle: String? = null


    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        this.mode = mode
        mode.menuInflater.inflate(menuResId, menu)
        mode.title = title
        mode.subtitle = subtitle
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        callback?.onActionItemClick(item)
        mode.finish()
        return true
    }


    override fun onDestroyActionMode(mode: ActionMode?) {
        this.mode = null
        callback?.onDestroyActionMode()
    }

    fun startActionMode(
        view: View,
        @MenuRes menuResId: Int,
        title: String? = null,
        subtitle: String? = null,
    ) {
        this.menuResId = menuResId
        this.title = title
        this.subtitle = subtitle
        view.startActionMode(this)
    }

    fun finishActionMode() {
        mode?.finish()
    }
}
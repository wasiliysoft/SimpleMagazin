package ru.wasiliysoft.simplemagazin.abc_list

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.wasiliysoft.simplemagazin.*
import ru.wasiliysoft.simplemagazin.data.SimpleItem
import ru.wasiliysoft.simplemagazin.main.MainViewModel

abstract class ListFragment(@LayoutRes layoutResId: Int) :
    Fragment(layoutResId),
    OnItemClick,
    OnItemLongClick {
    companion object {
        private const val LOG_TAG = "ListFragment"
    }


    abstract fun onDoubleItemClick(item: SimpleItem)

    protected val vm: MainViewModel by lazy {
        val fa = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ViewModelProvider(requireActivity(), fa).get(MainViewModel::class.java)
    }

    protected val simpleAdapter = SimpleAdapter()
    private var lastClickedId = ""
    private var lastClickedTime = 0L
    private val actionMode = PrimaryActionModeCallback()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        simpleAdapter.onItemClickCallback = this
        simpleAdapter.longClickCallback = this
    }

    override fun onItemClick(item: SimpleItem) {
        if (actionMode.mode != null) {
            simpleAdapter.selectItem(item)
        } else {
            if (System.currentTimeMillis() - lastClickedTime < 200) {
                if (item.id == lastClickedId) {
                    Log.d(LOG_TAG, "onDouble click")
                    onDoubleItemClick(item)
                }
            }
            lastClickedTime = System.currentTimeMillis()
            lastClickedId = item.id
        }
    }

    override fun onItemLongClick(item: SimpleItem) {
        actionMode.startActionMode(requireView(), R.menu.context_action_bar)
        actionMode.callback = object : ActionModeCallback {
            override fun onActionItemClick(item: MenuItem) {
                if (item.itemId == R.id.action_delete) {
                    vm.delete(simpleAdapter.getSelectedItem())
                }
            }

            override fun onDestroyActionMode() {
                simpleAdapter.clearAllSelectedFlags()
            }
        }
        simpleAdapter.selectItem(item)
    }
}
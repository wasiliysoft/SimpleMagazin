package ru.wasiliysoft.ssimplemagazin

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
fun interface RemoveItem {
    fun removeAt(position: Int)
}

class SwipeToDeleteCallback(private val adapter: RemoveItem) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT + ItemTouchHelper.LEFT) {
    override fun onMove(
        rv: RecyclerView,
        vh: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
        adapter.removeAt(vh.adapterPosition)
    }
}
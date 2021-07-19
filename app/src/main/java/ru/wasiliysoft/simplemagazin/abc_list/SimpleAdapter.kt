package ru.wasiliysoft.simplemagazin

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.wasiliysoft.simplemagazin.data.SimpleItem


fun interface OnItemClick {
    fun onItemClick(item: SimpleItem)
}

fun interface OnItemLongClick {
    fun onItemLongClick(item: SimpleItem)
}

class SimpleAdapter : RecyclerView.Adapter<SimpleAdapter.VH>() {

    var onItemClickCallback: OnItemClick? = null
    var longClickCallback: OnItemLongClick? = null

    private var items = listOf<SimpleItem>()

    fun setList(newList: List<SimpleItem>) {
        val diff = DiffUtil.calculateDiff(DiffUtilCallback(items, newList))
        items = newList
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent)

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClick(item)
        }
        holder.itemView.setOnLongClickListener {
            longClickCallback?.onItemLongClick(item)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount() = items.size

    fun clearAllSelectedFlags() {
        items.filter { it.selected }.forEach {
            val position = items.indexOf(it)
            items[position].selected = false
            notifyItemChanged(position)
        }
    }

    fun selectItem(si: SimpleItem) {
        val position = items.indexOf(si)
        items[position].selected = !items[position].selected
        notifyItemChanged(position)
    }

    fun getSelectedItem(): List<SimpleItem> {
        return items.filter { it.selected }
    }

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
    ) {
        private val title: TextView = itemView.findViewById(android.R.id.text1)
        fun bind(si: SimpleItem) {
            title.text = si.title
            itemView.isActivated = si.selected
        }
    }

    inner class DiffUtilCallback(
        private val oldList: List<SimpleItem>,
        private val newList: List<SimpleItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id === newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldProduct: SimpleItem = oldList[oldItemPosition]
            val newProduct: SimpleItem = newList[newItemPosition]
            return (oldProduct.title === newProduct.title
                    && oldProduct.isSuccess == newProduct.isSuccess)
                    && oldProduct.selected == newProduct.selected
        }
    }
}
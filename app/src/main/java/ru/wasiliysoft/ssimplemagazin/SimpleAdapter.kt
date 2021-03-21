package ru.wasiliysoft.ssimplemagazin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.wasiliysoft.ssimplemagazin.model.SimpleItem
import kotlin.concurrent.fixedRateTimer

fun interface OnItemClick {
    fun onItemClick(item: SimpleItem)
}

fun interface OnItemLongClick {
    fun onItemLongClick(item: SimpleItem)
}

class SimpleAdapter : RecyclerView.Adapter<SimpleAdapter.VH>() {
    var onItemClickCallback: OnItemClick? = null
    var longClickCallbacl: OnItemLongClick? = null


    var items: MutableList<SimpleItem> = MutableList(0) { SimpleItem("") }
        set(value) {
            if (field.size < value.size) {
                notifyItemRangeInserted(field.size, value.size - field.size)
                field = value
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent)

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClick(item)
        }
        holder.itemView.setOnLongClickListener {
            longClickCallbacl?.onItemLongClick(item)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount() = items.size

    fun addItem(si: SimpleItem) {
        items.add(si)
        notifyItemInserted(itemCount)
    }

    fun removeAt(position: Int) {
        items.remove(items[position])
        notifyItemRemoved(position)
    }

    fun deleteSelectedItem() {
        items.filter { it.selected }.forEach {
            removeAt(items.indexOf(it))
        }
    }

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

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
    ) {
        private val title: TextView = itemView.findViewById(android.R.id.text1)
        fun bind(si: SimpleItem) {
            title.text = si.title
            itemView.isActivated = si.selected
        }

    }
}
package ru.wasiliysoft.ssimplemagazin

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.wasiliysoft.ssimplemagazin.model.SimpleItem


class SimpleAdapter(
    private val items: MutableList<SimpleItem>
) : RecyclerView.Adapter<SimpleAdapter.VH>(), RemoveItem {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent)

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    fun getItems() = items
    override fun getItemCount() = items.size

    fun addItem(si: SimpleItem) {
        items.add(si)
        notifyItemInserted(itemCount)
    }

    override fun removeAt(position: Int) {
        items.remove(items[position])
        notifyItemRemoved(position)
    }

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
    ) {
        fun bind(si: SimpleItem) {
            itemView.findViewById<TextView>(android.R.id.text1).text = si.title
        }

    }
}
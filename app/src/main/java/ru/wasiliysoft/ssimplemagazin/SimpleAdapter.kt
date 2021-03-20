package ru.wasiliysoft.ssimplemagazin

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.wasiliysoft.ssimplemagazin.model.SimpleItem

fun interface DoubleClickView {
    fun onDoubleClick(id: String)
}

class SimpleAdapter : RecyclerView.Adapter<SimpleAdapter.VH>() {
    var doubleClickCallback: DoubleClickView? = null
    private var lastClickedId = ""
    private var lastClickedTime = 0L

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
            if (System.currentTimeMillis() - lastClickedTime < 200) {
                if (item.id == lastClickedId) {
                    doubleClickCallback?.onDoubleClick(item.id)
                }
            }
            lastClickedTime = System.currentTimeMillis()
            lastClickedId = item.id
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

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
    ) {
        fun bind(si: SimpleItem) {
            itemView.findViewById<TextView>(android.R.id.text1).text = si.title
        }

    }
}
package ru.wasiliysoft.simplemagazin.pending_list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.wasiliysoft.simplemagazin.R
import ru.wasiliysoft.simplemagazin.abc_list.ListFragment
import ru.wasiliysoft.simplemagazin.data.SimpleItem

class PendingListFragment : ListFragment(R.layout.fragment_pending_list) {
    companion object {
        private const val LOG_TAG = "PendingListFragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "onViewCreated")
        vm.list.observe(viewLifecycleOwner) { list ->
            simpleAdapter.setList(list.filter { !it.isSuccess })
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity()).apply {
                stackFromEnd = true
            }
            setHasFixedSize(true)
            adapter = simpleAdapter
        }

        val editText = view.findViewById<EditText>(R.id.editText)
        val button = view.findViewById<ImageButton>(R.id.button)
        button.setOnClickListener {
            if (editText.text.isBlank()) return@setOnClickListener
            vm.addItem(SimpleItem(editText.text.toString()))
            recyclerView.scrollToPosition(simpleAdapter.itemCount - 1)
            editText.text.clear()
        }
    }


    override fun onDoubleItemClick(item: SimpleItem) {
        vm.toSuccess(item.id)
    }

}
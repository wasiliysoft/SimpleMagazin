package ru.wasiliysoft.ssimplemagazin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.wasiliysoft.ssimplemagazin.model.SimpleItem

class PendingListFragment() : Fragment(R.layout.fragment_pending_list),
    DoubleClickView {
    companion object {
        private const val LOG_TAG = "PendingListFragment"
    }

    private lateinit var prefHelper: PrefHelper
    private lateinit var simpleAdapter: SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
        prefHelper = PrefHelper(requireContext())
        simpleAdapter = SimpleAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "onViewCreated")
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity()).apply {
                stackFromEnd = true
            }
            setHasFixedSize(true)
            adapter = simpleAdapter
        }
        simpleAdapter.doubleClickCallback = this
        simpleAdapter.longClickCallbacl = View.OnLongClickListener {
            val actionMode = PrimaryActionModeCallback()
            actionMode.startActionMode(view, R.menu.context_action_bar)
            true
        }
        val editText = view.findViewById<EditText>(R.id.editText)
        val button = view.findViewById<ImageButton>(R.id.button)
        button.setOnClickListener {
            if (editText.text.isBlank()) return@setOnClickListener
            simpleAdapter.addItem(SimpleItem(editText.text.toString()))
            recyclerView.scrollToPosition(simpleAdapter.itemCount - 1)
            editText.text.clear()
        }
    }


    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume")
        simpleAdapter.items = prefHelper.pendingList
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, "onPause")
        prefHelper.pendingList = simpleAdapter.items
    }

    override fun onDoubleClick(id: String) {
        Log.d(LOG_TAG, "doubleClick")
        simpleAdapter.items.find { it.id == id }?.let {
            val position = simpleAdapter.items.indexOf(it)
            val item = simpleAdapter.items[position]
            val successList = prefHelper.successList
            successList.add(item)
            prefHelper.successList = successList
            simpleAdapter.removeAt(position)
        }
    }
}

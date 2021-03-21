package ru.wasiliysoft.ssimplemagazin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.wasiliysoft.ssimplemagazin.model.SimpleItem

class SuccessListFragment() : ListFragment(R.layout.fragment_succes_list) {
    companion object {
        private const val LOG_TAG = "SuccessListFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "onViewCreated")
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = simpleAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume")
        simpleAdapter.items = prefHelper.successList
    }

    override fun saveList() {
        prefHelper.successList = simpleAdapter.items
    }

    override fun onDoubleItemClick(item: SimpleItem) {
        val position = simpleAdapter.items.indexOf(item)
        val pendingList = prefHelper.pendingList
        pendingList.add(item)
        prefHelper.pendingList = pendingList
        simpleAdapter.removeAt(position)
        saveList()
    }
}
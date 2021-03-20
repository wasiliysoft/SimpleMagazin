package ru.wasiliysoft.ssimplemagazin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SuccessListFragment() : Fragment(R.layout.fragment_succes_list),
    DoubleClickView {
    companion object {
        private const val LOG_TAG = "SuccessListFragment"
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
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = simpleAdapter
        }

        simpleAdapter.doubleClickCallback = this

    }

    override fun onDoubleClick(id: String) {
        Log.d(LOG_TAG, "doubleClick")
        simpleAdapter.items.find { it.id == id }?.let {
            val position = simpleAdapter.items.indexOf(it)
            val item = simpleAdapter.items[position]
            val pendingList = prefHelper.pendingList
            pendingList.add(item)
            prefHelper.pendingList = pendingList
            simpleAdapter.removeAt(position)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume")
        simpleAdapter.items = prefHelper.successList
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, "onPause")
        prefHelper.successList = simpleAdapter.items
    }
}
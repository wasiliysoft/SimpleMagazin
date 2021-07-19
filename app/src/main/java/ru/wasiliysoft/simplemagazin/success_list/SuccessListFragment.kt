package ru.wasiliysoft.simplemagazin.success_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.wasiliysoft.simplemagazin.R
import ru.wasiliysoft.simplemagazin.abc_list.ListFragment
import ru.wasiliysoft.simplemagazin.data.SimpleItem

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
        vm.list.observe(viewLifecycleOwner) { list ->
            simpleAdapter.setList(list.filter { it.isSuccess })
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = simpleAdapter
        }
    }

    override fun onDoubleItemClick(item: SimpleItem) {
        vm.toPending(item)
    }
}
package ru.wasiliysoft.ssimplemagazin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONStringer
import ru.wasiliysoft.ssimplemagazin.model.SimpleItem
import java.net.Proxy

class MainActivity : AppCompatActivity() {


    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.recyclerView)
    }
    private val prefHelper by lazy {
        PrefHelper(applicationContext)
    }

    private val simpleAdapter by lazy {
        val items: MutableList<SimpleItem>? = Gson().fromJson(
            prefHelper.jsonItemList,
            (object : TypeToken<MutableList<SimpleItem>>() {}).type
        )

        SimpleAdapter(items ?: MutableList(0) { SimpleItem("") })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = simpleAdapter


        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(simpleAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val editText = findViewById<EditText>(R.id.editText)
        val button = findViewById<ImageButton>(R.id.button)
        button.setOnClickListener {
            if (editText.text.isBlank()) return@setOnClickListener
            simpleAdapter.addItem(SimpleItem(editText.text.toString()))
            recyclerView.scrollToPosition(simpleAdapter.itemCount - 1)
            editText.text.clear()
        }
    }

    override fun onPause() {
        super.onPause()
        prefHelper.jsonItemList = Gson().toJson(simpleAdapter.getItems())
    }
}
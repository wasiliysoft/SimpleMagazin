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
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONStringer
import ru.wasiliysoft.ssimplemagazin.model.SimpleItem
import java.net.Proxy

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager: ViewPager = findViewById(R.id.pager)
        viewPager.adapter = PageAdapter(supportFragmentManager)

        val tabView: TabLayout = findViewById(R.id.tabLayout)
        tabView.setupWithViewPager(viewPager)

    }


}
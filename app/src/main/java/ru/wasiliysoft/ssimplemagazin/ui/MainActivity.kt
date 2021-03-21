package ru.wasiliysoft.ssimplemagazin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import ru.wasiliysoft.ssimplemagazin.R
import ru.wasiliysoft.ssimplemagazin.ui.fragment.PendingListFragment
import ru.wasiliysoft.ssimplemagazin.ui.fragment.SuccessListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager: ViewPager = findViewById(R.id.pager)
        viewPager.adapter = PageAdapter(supportFragmentManager)

        val tabView: TabLayout = findViewById(R.id.tabLayout)
        tabView.setupWithViewPager(viewPager)
    }

    inner class PageAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount() = 2

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> PendingListFragment()
            else -> SuccessListFragment()
        }

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.tab_pending)
            else -> getString(R.string.tab_success)
        }
    }
}
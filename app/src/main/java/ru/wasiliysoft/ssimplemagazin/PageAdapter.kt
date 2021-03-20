package ru.wasiliysoft.ssimplemagazin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PageAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount() = 2

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> PendingListFragment()
        else -> SuccessListFragment()
    }

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Купить"
        else -> "Куплено"
    }
}
package com.fmohammadi.whatsapp.controller.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fmohammadi.whatsapp.controller.fragments.ChatsFragment
import com.fmohammadi.whatsapp.controller.fragments.UsersFragment

class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return UsersFragment()
            1 -> return ChatsFragment()
        }
        return null!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Users"
            1 -> return "Chats"
        }
        return null!!
    }
}
package com.example.githubapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubapp.activity.FollowerFragment
import com.example.githubapp.activity.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, var username: String) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment(username)
            1 -> fragment = FollowingFragment(username)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}
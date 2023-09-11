package com.example.githubusernew.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubusernew.ui.detailuser.DetailFragment
import com.example.githubusernew.ui.follow.FollowersFragment
import com.example.githubusernew.ui.follow.FollowingsFragment

class SectionsPagerAdapter(fragment: FragmentActivity,private val userName: String?) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        val mBundle = Bundle()
        mBundle.putString(DetailFragment.EXTRA_USERNAME, userName)

        val fragmentFollower = FollowersFragment()
        fragmentFollower.arguments = mBundle

        val fragmentFollowing = FollowingsFragment()
        fragmentFollowing.arguments = mBundle

        when (position) {
            0 -> fragment = fragmentFollower
            1 -> fragment = fragmentFollowing
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}
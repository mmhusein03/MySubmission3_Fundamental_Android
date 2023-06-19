package com.dicoding.picodiploma.mysubmission2.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.picodiploma.mysubmission2.fragment.FragmentFollow

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var userName: String = ""

    override fun createFragment(position: Int): Fragment {
        val fragment = FragmentFollow()
        fragment.arguments = Bundle().apply {
            putInt(FragmentFollow.ARG_SECTION_NUMBER, position + 1)
            putString(FragmentFollow.USERNAME, userName)
        }
        return fragment
    }

    override fun getItemCount(): Int = 2
}
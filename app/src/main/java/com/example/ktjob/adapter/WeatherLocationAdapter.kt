package com.example.ktjob.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class WeatherLocationAdapter(var fmList: List<Fragment>, fm: FragmentManager?):
    FragmentPagerAdapter(fm as FragmentManager) {
    override fun getItem(pos: Int) = fmList[pos]

    override fun getCount() = fmList.size
}
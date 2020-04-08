package com.example.ktjob.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class WeatherLocationAdapter(var fmList: List<Fragment>, fm: FragmentManager?):
    FragmentStatePagerAdapter(fm as FragmentManager) {
    override fun getItem(pos: Int) = fmList[pos]

    override fun getCount() = fmList.size
}
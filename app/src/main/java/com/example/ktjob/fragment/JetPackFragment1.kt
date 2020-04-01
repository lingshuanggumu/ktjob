package com.example.ktjob.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.ktjob.R

class JetPackFragment1 : Fragment() {
    private var mTag = "JetPackFragment1"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //frag_show.text = "Fragment1"
        val test = listOf<String>("zhongguo", "shanghai", "pudong")
        test.map {
            "I love ".plus(it)
        }
        super.onViewCreated(view, savedInstanceState)
        Log.i(mTag, "onViewCreated")
    }
}
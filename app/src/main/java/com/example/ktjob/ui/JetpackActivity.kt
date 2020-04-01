package com.example.ktjob.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.navOptions
import com.example.ktjob.R
import com.example.ktjob.jetpack.JetPackObserver
import kotlinx.android.synthetic.main.activity_jetpack.*

class JetpackActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var mBtFrag1: Button

    private lateinit var mBtFrag2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jetpack)
        lifecycle.addObserver(JetPackObserver())
        mBtFrag1 = findViewById(R.id.btFrag1)
        mBtFrag2 = findViewById(R.id.btFrag2)

        mBtFrag1.setOnClickListener {
            //Navigation.findNavController(it).navigate(R.id.frag1_action)
        }

        mBtFrag2.setOnClickListener {
            //Navigation.findNavController(it).navigate(R.id.frag2_action)
        }
    }
}

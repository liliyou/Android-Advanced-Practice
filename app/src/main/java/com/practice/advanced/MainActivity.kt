package com.practice.advanced

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.practice.advanced.fragment.MainFragment
import com.practice.advanced.fragment.MainFragment2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, MainFragment2(), this.toString())
                    .commit()
        }
    }
}

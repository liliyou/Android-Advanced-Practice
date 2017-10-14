package com.practice.advanced.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practice.advanced.R
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by xuyating on 2017/9/30.
 */

class MainFragment : BaseFragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.btn_demo_schedulers.setOnClickListener { v ->
            clickOn(ConcurrencyWithSchedulersDemoFragment())
        }
        this.btn_demo_buffer.setOnClickListener { v ->
            clickOn(BufferDemoFragment())
        }
        this.btn_demo_debounce.setOnClickListener { v ->
            clickOn(DebounceSearchEmitterFragment())
        }
    }

    fun clickOn(fragment: Fragment) {
        val tag = fragment.javaClass.toString()
        activity
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack(tag)
                .replace(android.R.id.content, fragment, tag)
                .commit()
    }

}
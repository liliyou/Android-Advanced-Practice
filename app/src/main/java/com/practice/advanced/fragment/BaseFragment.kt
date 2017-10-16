package com.practice.advanced.fragment

import android.support.v4.app.Fragment
import com.practice.advanced.MyApp

/**
 * Created by xuyating on 2017/9/30.
 */

open class BaseFragment : Fragment() {
    override fun onDestroy() {
        super.onDestroy()
        MyApp.refWatcher().watch(this)
    }
}
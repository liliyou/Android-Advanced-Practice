package com.practice.advanced

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher



/**
 * Created by xuyating on 2017/9/30.
 */
class MyApp : Application() {

    private var mRefWatcher: RefWatcher? = null
    override fun onCreate() {
        super.onCreate()
        mRefWatcher = LeakCanary.install(this)
    }
}
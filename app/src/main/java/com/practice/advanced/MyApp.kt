package com.practice.advanced

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher


/**
 * Created by xuyating on 2017/9/30.
 */
class MyApp : Application() {


    companion object {
        private var instance: Application? = null
        private var refWatcher: RefWatcher? = null
        fun instance() = instance!!
        fun refWatcher() = refWatcher!!
    }

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        instance = this
        refWatcher = LeakCanary.install(this)
    }

}
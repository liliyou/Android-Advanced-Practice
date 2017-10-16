package com.practice.advanced.fragment.rxbus

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.practice.advanced.R
import com.practice.advanced.fragment.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_buffer.*
import java.util.concurrent.TimeUnit

/**
 * Created by xuyating on 2017/10/16.
 */

class RxBusDemoFragment : BaseFragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rxbus_demo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.demo_rxbus_frag_1, RxBusDemo_TopFragment())
                .replace(R.id.demo_rxbus_frag_2, RxBusDemo_Bottom3Fragment())
                //.replace(R.id.demo_rxbus_frag_2, new RxBusDemo_Bottom2Fragment())
                //.replace(R.id.demo_rxbus_frag_2, new RxBusDemo_Bottom1Fragment())
                .commit()
    }

    class TapEvent
}

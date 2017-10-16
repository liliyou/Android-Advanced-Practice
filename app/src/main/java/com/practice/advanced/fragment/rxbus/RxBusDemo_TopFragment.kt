package com.practice.advanced.fragment.rxbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practice.advanced.MainActivity
import com.practice.advanced.R
import com.practice.advanced.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_rxbus_top.*

/**
 * Created by xuyating on 2017/10/16.
 */
class RxBusDemo_TopFragment : BaseFragment() {

    private var rxBus: RxBus? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val layout = inflater.inflate(R.layout.fragment_rxbus_top, container, false)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rxBus = (getActivity() as MainActivity).getRxBusSingleton()
        this.btn_demo_rxbus_tap.setOnClickListener { v ->
            if (rxBus!!.hasObservers()) {
                rxBus!!.send(RxBusDemoFragment.TapEvent())
            }
        }
    }

}

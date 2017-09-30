package com.practice.advanced.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.practice.advanced.R


/**
 * Created by xuyating on 2017/9/30.
 */

class MainFragment : BaseFragment() {
    private lateinit var unbinder: Unbinder

    override fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var layout = inflater!!.inflate(R.layout.fragment_main, container, false)
        unbinder = ButterKnife.bind(this, layout as View)
        return layout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    @OnClick(R.id.btn_demo_schedulers)
    fun demoConcurrencyWithSchedulers() {
        Log.e("click", "demoConcurrencyWithSchedulers")
    }

}
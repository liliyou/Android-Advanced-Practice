package com.practice.advanced.fragment.rxbus

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practice.advanced.MainActivity
import com.practice.advanced.R
import com.practice.advanced.fragment.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_rxbus_bottom_3.*
import java.util.concurrent.TimeUnit

/**
 * Created by xuyating on 2017/10/16.
 */
class RxBusDemo_Bottom3Fragment : BaseFragment() {

    private lateinit var rxBus: RxBus
    private lateinit var disposables: CompositeDisposable

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val layout = inflater.inflate(R.layout.fragment_rxbus_bottom_3, container, false)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rxBus = (getActivity() as MainActivity).getRxBusSingleton()
    }

    override fun onStart() {
        super.onStart()
        disposables = CompositeDisposable()

        val tapEventEmitter = rxBus.asFlowable().publish()

        disposables
                .add(
                        tapEventEmitter.subscribe { event ->
                            if (event is RxBusDemoFragment.TapEvent) {
                                _showTapText()
                            }
                        })

        disposables.add(
                tapEventEmitter
                        .publish { stream -> stream.buffer(stream.debounce(1, TimeUnit.SECONDS)) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { taps ->
                            _showTapCount(taps.size)
                        })

        disposables.add(tapEventEmitter.connect())
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
        disposables.dispose()
    }

    // -----------------------------------------------------------------------------------
    // Helper to show the text via an animation

    private fun _showTapText() {
        this.demo_rxbus_tap_txt.setVisibility(View.VISIBLE)
        this.demo_rxbus_tap_txt.setAlpha(1f)
        ViewCompat.animate(this.demo_rxbus_tap_txt).alphaBy(-1f).duration = 400
    }

    private fun _showTapCount(size: Int) {
        this.demo_rxbus_tap_count.setText(size.toString())
        this.demo_rxbus_tap_count.setVisibility(View.VISIBLE)
        this.demo_rxbus_tap_count.setScaleX(1f)
        this.demo_rxbus_tap_count.setScaleY(1f)
        ViewCompat.animate(this.demo_rxbus_tap_count)
                .scaleXBy(-1f)
                .scaleYBy(-1f)
                .setDuration(800).startDelay = 100
    }

}
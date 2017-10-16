package com.practice.advanced.fragment.rxbus

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Created by xuyating on 2017/10/16.
 */
class RxBus {

    private val _bus = PublishRelay.create<Any>().toSerialized()

    fun send(o: Any) {
        _bus.accept(o)
    }

    fun asFlowable(): Flowable<Any> {
        //Drop就是直接把存不下的事件丢弃,Latest就是只保留最新的事件
        return _bus.toFlowable(BackpressureStrategy.LATEST)
    }

    fun hasObservers(): Boolean {
        return _bus.hasObservers()
    }
}
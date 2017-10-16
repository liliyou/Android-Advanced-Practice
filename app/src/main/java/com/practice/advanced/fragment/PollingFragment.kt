package com.practice.advanced.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.practice.advanced.R
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.fragment_polling.*
import org.reactivestreams.Publisher
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by xuyating on 2017/10/14.
 */
class PollingFragment : BaseFragment() {

    private val INITIAL_DELAY = 0
    private val POLLING_INTERVAL = 1000
    private val POLL_COUNT = 8

    private var counter = 0

    private lateinit var adapter: LogAdapter
    private lateinit var logs: MutableList<String>
    private var disposables = CompositeDisposable()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_polling, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupLogger()

        this.btn_start_simple_polling.setOnClickListener { v ->

            val pollCount = POLL_COUNT
            // doOnSubscribe在onNext之前做初始化操作。
            // interval 固定的時間間隔發射一個無限遞增的整數序列
            // take 是取前幾個發射的數據，或者在前一段時間內發射的數據。因為 interval 永遠執行
            val d = Flowable
                    .interval(INITIAL_DELAY.toLong(), POLLING_INTERVAL.toLong(), TimeUnit.MILLISECONDS)
                    .map<Any> { it -> this.doNetworkCallAndGetStringResult(it) }
                    .take(pollCount.toLong())
                    .doOnSubscribe { subscription -> _log(String.format("Start simple polling - %s", counter)) }
                    .subscribe { taskName ->
                        _log(
                                String.format(
                                        Locale.US,
                                        "Executing polled task [%s] now time : [xx:%02d]",
                                        taskName,
                                        getSecondHand()))
                    }

            disposables.add(d)
        }

        this.btn_start_increasingly_delayed_polling.setOnClickListener { v ->

            setupLogger()
            val pollingInterval = POLLING_INTERVAL
            val pollCount = POLL_COUNT

            _log(
                    String.format(
                            Locale.US, "Start increasingly delayed polling now time: [xx:%02d]", getSecondHand()))

            disposables.add(
                    Flowable.just(1L)
                            .repeatWhen(RepeatWithDelay(pollCount, pollingInterval))
                            .subscribe(
                                    { o ->
                                        _log(
                                                String.format(
                                                        Locale.US,
                                                        "Executing polled task now time : [xx:%02d]",
                                                        getSecondHand()))
                                    }
                            ) { e -> _log("arrrr. Error") })
        }
    }

    //public static class RepeatWithDelay
    inner class RepeatWithDelay internal constructor(private val _repeatLimit: Int, private val _pollingInterval: Int) : Function<Flowable<Any>, Publisher<Long>> {
        private var _repeatCount = 1

        // this is a notificationhandler, all we care about is
        // the emission "type" not emission "content"
        // only onNext triggers a re-subscription

        @Throws(Exception::class)
        override fun apply(inputFlowable: Flowable<Any>): Publisher<Long> {
            // it is critical to use inputObservable in the chain for the result
            // ignoring it and doing your own thing will break the sequence

            return inputFlowable.flatMap(
                    Function<Any, Publisher<Long>> {
                        if (_repeatCount >= _repeatLimit) {
                            // terminate the sequence cause we reached the limit
                            _log("Completing sequence")
                            return@Function Flowable.empty()
                        }

                        // since we don't get an input
                        // we store state in this handler to tell us the point of time we're firing
                        _repeatCount++

                        Flowable.timer((_repeatCount * _pollingInterval).toLong(), TimeUnit.MILLISECONDS)
                    })
        }
    }


    private fun doNetworkCallAndGetStringResult(attempt: Long): String {
        try {
            if (attempt == 4L) {
                // randomly make one event super long so we test that the repeat logic waits
                // and accounts for this.
                Thread.sleep(9000)
            } else {
                Thread.sleep(3000)
            }

        } catch (e: InterruptedException) {
//            Timber.d("Operation was interrupted")
        }

        counter++

        return counter.toString()
    }

    private fun getSecondHand(): Int {
        val millis = System.currentTimeMillis()
        return (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))).toInt()
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
        disposables.dispose()
    }


    private fun _log(logMsg: String) {

        if (isCurrentlyOnMainThread()) {
            logs?.add(0, logMsg + " (main thread) ")
            adapter?.clear()
            adapter?.addAll(logs)
        } else {
            logs?.add(0, logMsg + " (NOT main thread) ")

            // You can only do below stuff on main thread.
            Handler(Looper.getMainLooper())
                    .post {
                        adapter?.clear()
                        adapter?.addAll(logs)
                    }
        }
    }

    private fun isCurrentlyOnMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }

    private fun setupLogger() {
        logs = ArrayList<String>()
        adapter = LogAdapter(activity, ArrayList<String>())
        this.list_threading_log.setAdapter(adapter)
    }

    private inner class LogAdapter(context: Context, logs: List<String>) : ArrayAdapter<String>(context, R.layout.item_log, R.id.item_log, logs)

}

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
import kotlinx.android.synthetic.main.fragment_polling.*
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
            //doOnSubscribe 在onNext之前做初始化操作。
            //interval 固定的时间间隔发射一个无限递增的整数序列
            //take是取前几个发射的数据，或者在前一段时间内发射的数据。因為 interval 永远执行
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

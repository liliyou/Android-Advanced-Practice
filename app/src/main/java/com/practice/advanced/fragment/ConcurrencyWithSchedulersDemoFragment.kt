package com.practice.advanced.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.practice.advanced.R
import java.util.*

/**
 * Created by xuyating on 2017/9/30.
 */

class ConcurrencyWithSchedulersDemoFragment : BaseFragment() {

    @BindView(R.id.progress_operation_running)
    lateinit var progress: ProgressBar

    @BindView(R.id.list_threading_log)
    lateinit var logsList: ListView

    private lateinit var unbinder: Unbinder
    private lateinit var adapter: LogAdapter
    private lateinit var logs: MutableList<String>


    @OnClick(R.id.btn_start_operation)
    fun startLongOperation() {
        progress?.setVisibility(View.VISIBLE)
        _log("Button Clicked")
        doSomeLongOperation_thatBlocksCurrentThread()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var layout = inflater!!.inflate(R.layout.fragment_concurrency_schedulers, container, false)
        unbinder = ButterKnife.bind(this, layout as View)
        setupLogger()
        return layout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    private fun doSomeLongOperation_thatBlocksCurrentThread() {
        _log("performing long operation")

        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            _log("Operation was interrupted")
        }
        _log("Operation complete")

        progress?.setVisibility(View.GONE)
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
        logsList.setAdapter(adapter)
    }

    private inner class LogAdapter(context: Context, logs: List<String>) : ArrayAdapter<String>(context, R.layout.item_log, R.id.item_log, logs)

}
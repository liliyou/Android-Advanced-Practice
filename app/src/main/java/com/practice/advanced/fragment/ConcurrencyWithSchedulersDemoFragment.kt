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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_concurrency_schedulers.*

/**
 * Created by xuyating on 2017/9/30.
 */

class ConcurrencyWithSchedulersDemoFragment : BaseFragment() {

    private lateinit var adapter: LogAdapter
    private lateinit var logs: MutableList<String>
    private var disposables = CompositeDisposable()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_concurrency_schedulers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.btn_start_operation.setOnClickListener { v ->
            this.progress_operation_running.setVisibility(View.VISIBLE)
            _log("Button Clicked")
            disposables.add(
                    Observable.just(true)
                            .map { aBoolean ->
                                _log("Within Observable")
                                doSomeLongOperation_thatBlocksCurrentThread()
                                aBoolean
                            }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { result -> _log("On result") },
                                    { error ->
                                        _log(String.format("Boo! Error %s", error.message))
                                        this.progress_operation_running.setVisibility(View.INVISIBLE)
                                    },
                                    {
                                        _log("On complete")
                                        this.progress_operation_running.setVisibility(View.INVISIBLE)
                                    }
                            )
            )
        }

        setupLogger()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear();
        disposables.dispose();
    }

    private fun doSomeLongOperation_thatBlocksCurrentThread() {
        _log("performing long operation")

        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            _log("Operation was interrupted")
        }
        _log("Operation complete")
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
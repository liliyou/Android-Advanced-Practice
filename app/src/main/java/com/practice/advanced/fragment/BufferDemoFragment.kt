package com.practice.advanced.fragment

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
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.fragment_buffer.*
import java.util.concurrent.TimeUnit


/**
 * Created by xuyating on 2017/9/30.
 */

class BufferDemoFragment : BaseFragment() {

    private lateinit var adapter: LogAdapter
    private lateinit var logs: MutableList<String>
    private lateinit var disposable: Disposable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_buffer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupLogger()
    }

    override fun onResume() {
        super.onResume()
        disposable = getBufferedDisposable()
    }

    override fun onPause() {
        super.onPause()
        disposable.dispose()
    }

    private fun getBufferedDisposable(): Disposable {
        return RxView.clicks(this.btn_start_operation)
                .map<Any> { onClickEvent ->
                    _log("GOT A TAP")
                    1
                }.buffer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { next ->
                            if (next.size > 0) {
                                _log(String.format("%d taps", next.size))
                            } else {
                                _log("No taps received")
                            }
                        },
                        { error ->
                            _log(String.format("Boo! Error %s", error.message))
                        },
                        {
                            _log("On complete")
                        }
                )
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

package com.practice.advanced.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.practice.advanced.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_debounce.*
import java.lang.String.format
import java.util.concurrent.TimeUnit

/**
 * Created by xuyating on 2017/9/30.
 */

class DebounceSearchEmitterFragment : BaseFragment() {

    private lateinit var adapter: LogAdapter
    private lateinit var logs: MutableList<String>
    private lateinit var disposable: Disposable

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debounce, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupLogger()

        this.clr_debounce.setOnClickListener { v ->
            logs = ArrayList<String>()
            adapter.clear()
        }
        disposable = RxTextView
                .textChangeEvents(this.input_txt_debounce)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter { charSequence -> !charSequence.text().isEmpty()}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { charSequence ->
                            _log(format("Searching for %s", charSequence.text().toString()))
                        },
                        { error ->
                            _log(String.format("Boo! Error %s", error.message))
                        },
                        {
                            _log("On complete")
                        })
    }

    override fun onPause() {
        super.onPause()
        disposable.dispose()
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

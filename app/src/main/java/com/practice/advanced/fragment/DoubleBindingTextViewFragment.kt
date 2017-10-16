package com.practice.advanced.fragment

import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.practice.advanced.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.fragment_double_binding_textview.*

/**
 * Created by xuyating on 2017/9/30.
 */

class DoubleBindingTextViewFragment : BaseFragment() {

    private var disposables = CompositeDisposable()
    private lateinit var _resultEmitterSubject: PublishProcessor<Float>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_double_binding_textview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        disposables.add(RxTextView
                .textChangeEvents(this.double_binding_num1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            onNumberChanged()
                        }))
        disposables.add(RxTextView
                .textChangeEvents(this.double_binding_num2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            onNumberChanged()
                        }))

        _resultEmitterSubject = PublishProcessor.create<Float>()

        disposables.add(_resultEmitterSubject
                .subscribe { aFloat -> this.double_binding_result.setText(aFloat.toString()) })

        onNumberChanged()
        this.double_binding_num2.requestFocus()
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
        disposables.dispose()
    }


    fun onNumberChanged() {

        var num1 = 0f
        var num2 = 0f

        if (!isEmpty(this.double_binding_num1.getText().toString())) {
            num1 = java.lang.Float.parseFloat(this.double_binding_num1.getText().toString())
        }

        if (!isEmpty(this.double_binding_num2.getText().toString())) {
            num2 = java.lang.Float.parseFloat(this.double_binding_num2.getText().toString())
        }

        _resultEmitterSubject.onNext(num1 + num2)
    }
}

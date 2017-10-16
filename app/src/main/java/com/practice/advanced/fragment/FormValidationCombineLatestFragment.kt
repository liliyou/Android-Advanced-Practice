package com.practice.advanced.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils.isEmpty
import android.util.Patterns.EMAIL_ADDRESS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.practice.advanced.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.Function3
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.fragment_form_validation_comb_latest.*
import org.reactivestreams.Subscriber

/**
 * Created by xuyating on 2017/9/30.
 */

class FormValidationCombineLatestFragment : BaseFragment() {

    private lateinit var disposableObserver: DisposableSubscriber<Boolean>
    private lateinit var _emailChangeObservable: Flowable<CharSequence>
    private lateinit var _numberChangeObservable: Flowable<CharSequence>
    private lateinit var _passwordChangeObservable: Flowable<CharSequence>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_validation_comb_latest, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        disposableObserver.dispose()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _emailChangeObservable = RxTextView.textChanges(this.demo_combl_email).skip(1).toFlowable(BackpressureStrategy.LATEST)
        _passwordChangeObservable = RxTextView.textChanges(this.demo_combl_password).skip(1).toFlowable(BackpressureStrategy.LATEST)
        _numberChangeObservable = RxTextView.textChanges(this.demo_combl_num).skip(1).toFlowable(BackpressureStrategy.LATEST)
        combineLatestEvents()
    }

    private fun combineLatestEvents() {

        disposableObserver = object : DisposableSubscriber<Boolean>() {
            override fun onNext(formValid: Boolean?) {
                if (formValid!!) {
                    btn_demo_form_valid.setBackgroundColor(
                            ContextCompat.getColor(context, R.color.colorAccent))
                } else {
                    btn_demo_form_valid.setBackgroundColor(
                            ContextCompat.getColor(context, R.color.colorPrimaryDark))
                }
            }

            override fun onError(e: Throwable) {
//                Timber.e(e, "there was an error")
            }

            override fun onComplete() {
//                Timber.d("completed")
            }
        }
        Flowable.combineLatest(
                _emailChangeObservable,
                _passwordChangeObservable,
                _numberChangeObservable,
                object : Function3<CharSequence, CharSequence, CharSequence, Any> {
                    override fun apply(newEmail: CharSequence, newPassword: CharSequence, newNumber: CharSequence): Any {
                        val emailValid = !isEmpty(newEmail) && EMAIL_ADDRESS.matcher(newEmail).matches()
                        if (!emailValid) {
                            demo_combl_email.setError("Invalid Email!")
                        }

                        val passValid = !isEmpty(newPassword) && newPassword.length > 8
                        if (!passValid) {
                            demo_combl_password.setError("Invalid Password!")
                        }

                        var numValid = !isEmpty(newNumber)
                        if (numValid) {
                            val num = Integer.parseInt(newNumber.toString())
                            numValid = num > 0 && num <= 100
                        }
                        if (!numValid) {
                            demo_combl_num.setError("Invalid Number!")
                        }

                        return emailValid && passValid && numValid
                    }
                }).subscribe(disposableObserver as Subscriber<in Any>)
    }
}

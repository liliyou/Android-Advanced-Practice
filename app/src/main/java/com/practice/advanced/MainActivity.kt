package com.practice.advanced

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.practice.advanced.fragment.MainFragment
import com.practice.advanced.fragment.rxbus.RxBus

class MainActivity : AppCompatActivity() {

    private var rxBus: RxBus = RxBus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, MainFragment(), this.toString())
                    .commit()
        }
    }


    // This is better done with a DI Library like Dagger
    fun getRxBusSingleton(): RxBus {
        if (rxBus == null) {
            rxBus = RxBus()
        }
        return rxBus
    }
}

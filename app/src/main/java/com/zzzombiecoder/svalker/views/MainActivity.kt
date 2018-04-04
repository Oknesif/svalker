package com.zzzombiecoder.svalker.views

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zzzombiecoder.svalker.R
import com.zzzombiecoder.svalker.service.EX_DOUBLE_ARRAY
import com.zzzombiecoder.svalker.service.SVALKER_ACTION
import com.zzzombiecoder.svalker.service.SvalkerService
import com.zzzombiecoder.svalker.utils.plusAssign
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var dummySpectrumView: DummySpectrumView
    private lateinit var stopServiceButton: View
    private val disposable: CompositeDisposable = CompositeDisposable()

    private val serviceIntent: Intent by lazy { Intent(this, SvalkerService::class.java) }
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val data = intent.getDoubleArrayExtra(EX_DOUBLE_ARRAY)
            dummySpectrumView.feedData(data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dummySpectrumView = findViewById(R.id.show_me_view)
        stopServiceButton = findViewById(R.id.stop_service_button)

        stopServiceButton.setOnClickListener { stopService(serviceIntent) }
        disposable += Observable.interval(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    dummySpectrumView.invalidate()
                }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(broadcastReceiver, IntentFilter(SVALKER_ACTION))
        disposable += RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe({
                    if (it) {
                        startService(serviceIntent)
                    }
                }, {
                    Log.d("MainActivity", Log.getStackTraceString(it))
                })
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(broadcastReceiver)
    }
}

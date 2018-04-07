package com.zzzombiecoder.svalker.views

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zzzombiecoder.svalker.R
import com.zzzombiecoder.svalker.service.ServiceBinder
import com.zzzombiecoder.svalker.service.SvalkerService
import com.zzzombiecoder.svalker.utils.plusAssign
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var dummySpectrumView: DummySpectrumView
    private lateinit var stopServiceButton: View
    private lateinit var disposable: CompositeDisposable

    private val serviceIntent: Intent by lazy { Intent(this, SvalkerService::class.java) }
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            finish()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            disposable += Observable.interval(100, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        dummySpectrumView.invalidate()
                    }
            disposable += (service as ServiceBinder)
                    .getSpectrumData()
                    .subscribe {
                        dummySpectrumView.feedData(it)
                    }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dummySpectrumView = findViewById(R.id.show_me_view)
        stopServiceButton = findViewById(R.id.stop_service_button)
        stopServiceButton.setOnClickListener { stopService(serviceIntent) }
    }

    override fun onStart() {
        super.onStart()
        disposable = CompositeDisposable()
        disposable += RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe({
                    if (it) {
                        startService(serviceIntent)
                        bindService(serviceIntent, serviceConnection, 0)
                    }
                }, {
                    Log.d("MainActivity", Log.getStackTraceString(it))
                })
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        disposable.dispose()
    }
}

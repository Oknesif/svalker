package com.zzzombiecoder.svalker

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var showMeView: ShowMeView
    private val recorder = Recorder()
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showMeView = findViewById(R.id.show_me_view)

        Observable.interval(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    showMeView.invalidate()
                }
    }

    override fun onStart() {
        super.onStart()

        startService(Intent(this, SvalkerService::class.java))

        disposable = RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO)
                .flatMap {
                    if (it) {
                        recorder.getSpectrumAmpDB(50)
                    } else {
                        Observable.empty()
                    }
                }
                .subscribe({

                    Log.d("MainActivity", "Length: ${it.size}, max value: ${it.max()} at position ${it.maxPosition()}")
                    showMeView.feedData(it)
                }, {
                    Log.d("MainActivity", Log.getStackTraceString(it))
                })
    }

    private fun DoubleArray.maxPosition(): Int {
        if (isEmpty()) return -1
        var maxPosition = 0
        var max = this[0]
        if (max.isNaN()) return 0
        for (i in 1..lastIndex) {
            val e = this[i]
            if (e.isNaN()) return i
            if (max < e) {
                max = e
                maxPosition = i
            }
        }
        return maxPosition
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

}

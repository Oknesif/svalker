@file:Suppress("NOTHING_TO_INLINE")

package com.zzzombiecoder.svalker.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.zzzombiecoder.svalker.spectrum.analysis.Recorder
import io.reactivex.disposables.Disposable


class SvalkerService : Service() {

    private val recorder: Recorder
            by lazy { Recorder() }
    private val notificationController: NotificationController
            by lazy { NotificationController(this) }

    private lateinit var disposable: Disposable

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        notificationController.onCreate()
        disposable = recorder.getSpectrumAmpDB(50)
                .subscribe({
                    LocalBroadcastManager
                            .getInstance(this)
                            .sendBroadcast(Intent().apply {
                                putExtra(EX_SPECTRUM_DATA, it)
                                action = SVALKER_ACTION
                            })
                }, {
                    Log.e("SvalkerService", "getSpectrumAmpDB", it)
                })
        super.onCreate()
    }

    override fun onDestroy() {
        notificationController.onDestroy()
        disposable.dispose()
        super.onDestroy()
    }

}

const val SVALKER_ACTION: String = "SVALKER_ACTION"
const val EX_SPECTRUM_DATA: String = "EX_SPECTRUM_DATA"

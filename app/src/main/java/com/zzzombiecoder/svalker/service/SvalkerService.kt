@file:Suppress("NOTHING_TO_INLINE")

package com.zzzombiecoder.svalker.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.zzzombiecoder.svalker.spectrum.analysis.Recorder
import com.zzzombiecoder.svalker.spectrum.analysis.SpectrumData
import com.zzzombiecoder.svalker.state.State
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject


class SvalkerService : Service() {
    private val notificationController: NotificationController
            by lazy { NotificationController(this) }

    private val stateSubject: Subject<State> = BehaviorSubject.create()
    private val spectrumDataSubject: Subject<SpectrumData> = BehaviorSubject.create()
    private lateinit var disposable: Disposable

    override fun onBind(intent: Intent?): IBinder {
        return object : ServiceBinder, Binder() {

            override fun getStateUpdates(): Observable<State> {
                return stateSubject
            }

            override fun getSpectrumData(): Observable<SpectrumData> {
                Log.d("Service", "getSpectrumData")
                return spectrumDataSubject
            }
        }
    }

    override fun onCreate() {
        Handler()
        notificationController.onCreate()
        disposable = Recorder().getSpectrumAmpDB(50)
                .subscribe({
                    spectrumDataSubject.onNext(it)
                }, {
                    Log.e("SvalkerService", "getSpectrumAmpDB", it)
                })
        super.onCreate()
    }

    override fun onDestroy() {
        Log.d("Service", "Service is destroyed")
        notificationController.onDestroy()
        disposable.dispose()
        super.onDestroy()
    }
}
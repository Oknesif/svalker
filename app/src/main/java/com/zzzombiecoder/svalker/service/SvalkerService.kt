@file:Suppress("NOTHING_TO_INLINE")

package com.zzzombiecoder.svalker.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.zzzombiecoder.svalker.spectrum.analysis.Recorder
import com.zzzombiecoder.svalker.spectrum.analysis.SpectrumData
import com.zzzombiecoder.svalker.state.State
import com.zzzombiecoder.svalker.state.toSignal
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject


class SvalkerService : Service() {
    private val notificationController: NotificationController
            by lazy { NotificationController(this) }

    private val stateSubject: Subject<State> = BehaviorSubject.create()
    private val spectrumDataSubject: Subject<SpectrumData> = BehaviorSubject.create()

    private lateinit var disposable: CompositeDisposable

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
        notificationController.onCreate()
        disposable = CompositeDisposable()
        val spectrumData = Recorder().getSpectrumAmpDB(50)
                .subscribe({
                    spectrumDataSubject.onNext(it)
                }, {
                    Log.e("SvalkerService", "getSpectrumAmpDB", it)
                })
        val signalChecker = spectrumDataSubject
                .toSignal()
                .subscribe {
                    Log.d("SvalkerService", "Emitted signal: $it")
                }

        disposable = CompositeDisposable(spectrumData, signalChecker)
        super.onCreate()
    }

    override fun onDestroy() {
        notificationController.onDestroy()
        disposable.dispose()
        super.onDestroy()
    }
}
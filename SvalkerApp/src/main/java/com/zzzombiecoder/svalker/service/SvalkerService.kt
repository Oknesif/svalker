@file:Suppress("NOTHING_TO_INLINE")

package com.zzzombiecoder.svalker.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.zzzombiecoder.svalker.spectrum.analysis.Recorder
import com.zzzombiecoder.svalker.spectrum.analysis.SpectrumData
import com.zzzombiecoder.svalker.state.*
import com.zzzombiecoder.svalker.state.effects.LifeEffect
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit


class SvalkerService : Service() {
    private val notificationController: NotificationController
            by lazy { NotificationController(this) }

    private val stateSubject: Subject<State> = BehaviorSubject.create()
    private val spectrumDataSubject: Subject<SpectrumData> = BehaviorSubject.create()
    private val signalSubject: Subject<SignalType> = BehaviorSubject.create()
    private val commandSubject: Subject<Command> = BehaviorSubject.create()

    private lateinit var disposable: CompositeDisposable

    override fun onBind(intent: Intent?): IBinder {
        return object : ServiceBinder, Binder() {
            override fun setCommandSource(commandSource: Observable<Command>) {
                commandSource.subscribe(commandSubject)
            }

            override fun getStateUpdates(): Observable<State> {
                return stateSubject.observeOn(AndroidSchedulers.mainThread())
            }

            override fun getSignal(): Observable<SignalType> {
                return signalSubject.observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

    override fun onCreate() {
        startForeground(NOTIFICATION_ID.hashCode(), notificationController.createNotification())

        disposable = CompositeDisposable()

        val life = Observable.interval(1, TimeUnit.SECONDS)
                .map { LifeEffect() }
        val spectrumData = Recorder().getSpectrumAmpDB(50)
                .subscribe({
                    spectrumDataSubject.onNext(it)
                }, {
                    Log.e("SvalkerService", "getSpectrumAmpDB", it)
                })
        val signals = spectrumDataSubject
                .toSignalType()
                .subscribe { signalSubject.onNext(it) }
        val signalEffects = signalSubject.toEffects()
        val commands = commandSubject.map { it.toEffect() }
        val effects = Observable.merge(life, signalEffects, commands)

        val stateChanges = effects
                .scan(getInitialState()) { state, effect ->
                    effect.apply(state)
                }.subscribe {
                    stateSubject.onNext(it)
                }

        disposable = CompositeDisposable(spectrumData, stateChanges, signals)
        super.onCreate()
    }

    private fun getInitialState(): State {
        return State.NotInGame()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
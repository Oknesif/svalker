package com.zzzombiecoder.svalker.service

import android.content.Context
import android.os.Vibrator
import com.zzzombiecoder.svalker.state.SignalType
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class VibrationController(context: Context) {
    private val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private var disposable: Disposable? = null

    private val signalsToVibrate: Array<SignalType> = arrayOf(
            SignalType.Electra,
            SignalType.Studen,
            SignalType.Inferno,
            SignalType.Psy_emmiter,
            SignalType.Psy_controller,
            SignalType.Radiation1,
            SignalType.Radiation2,
            SignalType.Radiation3,
            SignalType.Radiation4,
            SignalType.Radiation5
    )

    fun isEnabled(): Boolean {
        return true
    }

    fun subscribe(signalType: Observable<SignalType>) {
        disposable = signalType.subscribe {
            if (isEnabled() && signalsToVibrate.contains(it)) {
                vibrator.vibrate(200)
            }
        }
    }

    fun unsubscribe() {
        disposable?.dispose()
    }
}
package com.zzzombiecoder.svalker.service

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.zzzombiecoder.svalker.state.SignalType
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class VibrationController(context: Context) {
    private val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    var isEnabled: Boolean = false

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
            SignalType.Radiation5,
            SignalType.Unplugged
    )

    fun subscribe(signalType: Observable<SignalType>): Disposable {
        return signalType.subscribe {
            if (isEnabled && vibrator.hasVibrator() && signalsToVibrate.contains(it)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val vibrationEffect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.vibrate(vibrationEffect)
                } else {
                    vibrator.vibrate(200)
                }
            }
        }
    }
}
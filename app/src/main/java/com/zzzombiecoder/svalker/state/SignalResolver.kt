package com.zzzombiecoder.svalker.state

import android.os.SystemClock
import com.zzzombiecoder.svalker.spectrum.analysis.SpectrumData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

fun Observable<SpectrumData>.toSignal(): Observable<Signal> {
    return this.compose(SpectrumAnalyser())
}

private class SpectrumAnalyser : ObservableTransformer<SpectrumData, Signal> {
    override fun apply(upstream: Observable<SpectrumData>): ObservableSource<Signal> {
        var firstInSequence: Pair<Signal, Long> = emptyRecord()

        return Observable.create<Signal> { emitter ->
            val disposable = upstream.subscribe({
                val timeStamp = SystemClock.uptimeMillis()
                val signal: Signal = if (isLoudEnough(it)) getSignal(it) else Signal.None
                val (recodedSignal, recodedTimeStamp) = firstInSequence
                if (recodedSignal == signal) {
                    if ((timeStamp - recodedTimeStamp) >= signal.timePeriodMls) {
                        firstInSequence = emptyRecord()
                        emitter.onNext(signal)
                    }
                } else {
                    firstInSequence = Pair(signal, timeStamp)
                }
            }, {
                emitter.onError(it)
            }, {
                emitter.onComplete()
            })
            emitter.setCancellable {
                disposable.dispose()
            }
        }
    }

    private fun isLoudEnough(spectrumData: SpectrumData): Boolean =
            spectrumData.amplitudeArray.max() ?: Double.MIN_VALUE > MIN_DB

    private fun getSignal(spectrumData: SpectrumData): Signal {
        for (signal in Signal.values()) {
            if (signal.frequencyRange.isIn(spectrumData.maxAmpFreq)) {
                return signal
            }
        }
        return Signal.None
    }

    private fun emptyRecord(): Pair<Signal, Long> = Pair(Signal.None, 0L)
}
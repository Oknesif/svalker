package com.zzzombiecoder.svalker.state

import com.zzzombiecoder.svalker.spectrum.analysis.SpectrumData
import com.zzzombiecoder.svalker.state.effects.Effect
import com.zzzombiecoder.svalker.state.effects.sequences.IEffectSequence
import com.zzzombiecoder.svalker.state.effects.sequences.NoneEffectSequence
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

fun Observable<SpectrumData>.toSignalType(): Observable<SignalType> {
    return this.map {
        if (isLoudEnough(it)) getSignal(it) else SignalType.None
    }
}

private fun isLoudEnough(spectrumData: SpectrumData): Boolean =
        spectrumData.amplitudeArray.max() ?: Double.MIN_VALUE > MIN_DB

private fun getSignal(spectrumData: SpectrumData): SignalType {
    for (signal in SignalType.values()) {
        val frequencyRange = SignalsByFrequency[signal]
        if (frequencyRange!!.isIn(spectrumData.maxAmpFreq)) {
            return signal
        }
    }
    return SignalType.None
}

fun Observable<SignalType>.toEffects(): Observable<Effect> {
    return this.compose(SpectrumAnalyser())
}

private class SpectrumAnalyser : ObservableTransformer<SignalType, Effect> {
    override fun apply(upstream: Observable<SignalType>): ObservableSource<Effect> {
        var firstInSequence: SignalType = SignalType.None
        var effectSequence: IEffectSequence = NoneEffectSequence()

        return Observable.create<Effect> { emitter ->
            val disposable = upstream.subscribe({
                if (firstInSequence == it) {
                    val effect = effectSequence.registerSignal()
                    effect?.let {
                        emitter.onNext(it)
                    }
                } else {
                    firstInSequence = it
                    effectSequence = getEffectSequenceBySignal(it)
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
}
package com.zzzombiecoder.svalker.state

import com.zzzombiecoder.svalker.state.effects.*
import com.zzzombiecoder.svalker.state.effects.sequences.EffectSequence
import com.zzzombiecoder.svalker.state.effects.sequences.IEffectSequence
import com.zzzombiecoder.svalker.state.effects.sequences.NoneEffectSequence
import java.util.concurrent.TimeUnit

enum class SignalType {
    Electra,
    Studen,
    Inferno,
    Psy_emmiter,
    Psy_controller,
    Graveyard,
    Radiation1,
    Radiation2,
    Radiation3,
    Radiation4,
    Radiation5,
    Unplugged,
    None
}

object SignalsByFrequency : HashMap<SignalType, FrequencyRange>() {
    init {
        for (signalType in SignalType.values()) {
            val frequencyRange: FrequencyRange = when (signalType) {
                SignalType.Psy_emmiter -> {
                    FrequencyRange(70.0, 110.0)
                }
                SignalType.Psy_controller -> {
                    FrequencyRange(130.0, 150.0)
                }
                SignalType.Inferno -> {
                    FrequencyRange(370.0, 410.0)
                }
                SignalType.Studen -> {
                    FrequencyRange(280.0, 320.0)
                }
                SignalType.Electra -> {
                    FrequencyRange(228.0, 250.0)
                }
                SignalType.Graveyard -> {
                    FrequencyRange(145.0, 160.0)
                }
                SignalType.Radiation1 -> {
                    FrequencyRange(1000.0, 1080.0)
                }
                SignalType.Radiation2 -> {
                    FrequencyRange(1080.0, 1160.0)
                }
                SignalType.Radiation3 -> {
                    FrequencyRange(1160.0, 1240.0)
                }
                SignalType.Radiation4 -> {
                    FrequencyRange(1240.0, 1320.0)
                }
                SignalType.Radiation5 -> {
                    FrequencyRange(1320.0, 1400.0)
                }
                SignalType.None -> {
                    FrequencyRange(0.0, 3000.0)
                }
                SignalType.Unplugged -> {
                    FrequencyRange(-1.0, -1.0)
                }
            }
            put(signalType, frequencyRange)
        }
    }
}

fun getEffectSequenceBySignal(signalType: SignalType): IEffectSequence {
    return when (signalType) {
        SignalType.Unplugged -> {
            EffectSequence(DeathEffect(CauseOfDeath.DETECTOR_DETACHED), 10L, TimeUnit.SECONDS)
        }
        SignalType.None -> {
            NoneEffectSequence()
        }
        SignalType.Psy_emmiter -> {
            EffectSequence(PsyEmitterEffect(), 1L, TimeUnit.SECONDS)
        }
        SignalType.Psy_controller -> {
            EffectSequence(PsyControllerEffect(), 1L, TimeUnit.SECONDS)
        }
        SignalType.Graveyard -> {
            EffectSequence(GraveyardEffect(), 1L, TimeUnit.SECONDS)
        }
        SignalType.Inferno -> {
            EffectSequence(ReduceHealthEffect(20.0), 1L, TimeUnit.SECONDS)
        }
        SignalType.Studen -> {
            val periods = LongArray(30) { 1000L }
            val effect = Array<Effect>(30) { ReduceHealthEffect(it * 5.0 + 5.0) }
            EffectSequence(periods, effect)
        }
        SignalType.Electra -> {
            val periods: LongArray = longArrayOf(500L, 500L, 500L, 500L)
            val effects: Array<Effect> = arrayOf(
                    ReduceHealthEffect(25.0),
                    ReduceHealthEffect(15.0),
                    ReduceHealthEffect(5.0),
                    ReduceHealthEffect(0.0)
            )
            EffectSequence(periods, effects)
        }
        SignalType.Radiation1 -> {
            EffectSequence(RadiationSpotEffect(1.0), 1L, TimeUnit.SECONDS)
        }
        SignalType.Radiation2 -> {
            EffectSequence(RadiationSpotEffect(2.0), 1L, TimeUnit.SECONDS)
        }
        SignalType.Radiation3 -> {
            EffectSequence(RadiationSpotEffect(3.0), 1L, TimeUnit.SECONDS)
        }
        SignalType.Radiation4 -> {
            EffectSequence(RadiationSpotEffect(4.0), 1L, TimeUnit.SECONDS)
        }
        SignalType.Radiation5 -> {
            EffectSequence(RadiationSpotEffect(5.0), 1L, TimeUnit.SECONDS)
        }
    }
}
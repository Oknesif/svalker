package com.zzzombiecoder.svalker.state.effects.sequences

import com.zzzombiecoder.svalker.state.effects.Effect

interface IEffectSequence {
    fun registerSignal(): Effect?
}
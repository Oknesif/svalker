package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.Effect

interface IEffectSequence {
    fun registerSignal(): Effect?
}
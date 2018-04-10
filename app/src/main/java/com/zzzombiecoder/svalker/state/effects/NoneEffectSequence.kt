package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.Effect

class NoneEffectSequence : IEffectSequence {
    override fun registerSignal(): Effect? = null
}
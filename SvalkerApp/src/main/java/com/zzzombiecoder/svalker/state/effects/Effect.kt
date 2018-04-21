package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.*

interface Effect {
    fun apply(state: State): State
}


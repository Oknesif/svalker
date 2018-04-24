package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.CauseOfDeath
import com.zzzombiecoder.svalker.state.State

class DeathEffect(private val causeOfDeath: CauseOfDeath) : Effect {
    override fun apply(state: State): State {
        return if (state is State.Normal) {
            State.Dead(cause = causeOfDeath)
        } else {
            state
        }
    }
}
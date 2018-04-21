package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.CauseOfDeath
import com.zzzombiecoder.svalker.state.State

class DeathEffect : Effect {
    override fun apply(state: State): State {
        return State.Dead(cause = CauseOfDeath.SUICIDE)
    }
}
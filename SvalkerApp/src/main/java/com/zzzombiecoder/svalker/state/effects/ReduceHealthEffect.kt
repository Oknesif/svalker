package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.CauseOfDeath
import com.zzzombiecoder.svalker.state.State

class ReduceHealthEffect(private val reducingPoints: Double) : Effect {
    override fun apply(state: State): State {
        return if (state is State.Normal) {
            val newHp = state.health - reducingPoints
            if (newHp <= 0) {
                State.Dead(cause = CauseOfDeath.ANOMALY)
            } else {
                state.copy(health = newHp)
            }
        } else {
            state
        }
    }
}
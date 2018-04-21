package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.State

class GraveyardEffect : Effect {
    override fun apply(state: State): State {
        return if (state is State.Dead) {
            val timeToRespawnLeft = state.timeToRespawnSeconds - 1
            if (timeToRespawnLeft <= 0) {
                State.Normal()
            } else {
                state.copy(timeToRespawnLeft)
            }
        } else {
            state
        }
    }
}
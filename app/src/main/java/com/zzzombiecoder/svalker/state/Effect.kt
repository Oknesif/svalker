package com.zzzombiecoder.svalker.state

interface Effect {
    fun apply(state: State): State
}

class Life() : Effect {
    override fun apply(state: State): State {
        return if (state is State.Normal) {
            val newHealthPoints = state.healthPoints - Math.pow(state.radiationLevel, 2.0)
            if (newHealthPoints <= 0) {
                State.Dead()
            } else {
                state.copy(healthPoints = newHealthPoints)
            }
        } else {
            state
        }
    }
}

class Graveyard() : Effect {
    override fun apply(state: State): State {
        return if (state is State.Dead) {
            val timeToRespawnLeft = state.timeToRespawnSeconds - 1
            if (timeToRespawnLeft <= 0) {
                State.Normal()
            } else {
                State.Dead(timeToRespawnLeft)
            }
        } else {
            state
        }
    }
}

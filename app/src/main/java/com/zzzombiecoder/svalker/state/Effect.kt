package com.zzzombiecoder.svalker.state

interface Effect {
    fun apply(state: State): State
}


private fun State.Normal.calculateStateByHp(hp: Double): State {
    return when {
        hp <= 0 -> State.Dead()
        hp > MAX_HEALTH -> this.copy(healthPoints = MAX_HEALTH)
        else -> this.copy(healthPoints = hp)
    }
}

class Revive : Effect {
    override fun apply(state: State): State {
        return State.Normal()
    }

}

class Death : Effect {
    override fun apply(state: State): State {
        return State.Dead()
    }

}

class None : Effect {
    override fun apply(state: State): State {
        return state
    }
}

class Electra : Effect {
    override fun apply(state: State): State {
        return if (state is State.Normal) {
            state.calculateStateByHp(state.healthPoints - 30)
        } else {
            state
        }
    }

}

class RadiationSpot : Effect {

    private val additionRadiationLevel = 3L

    override fun apply(state: State): State {
        return if (state is State.Normal) {
            state.copy(radiationLevel = state.radiationLevel + additionRadiationLevel)
        } else {
            state
        }
    }
}

class Life : Effect {

    private val radiationHpCoff = 0.3
    private val hpRegen = 0.014

    override fun apply(state: State): State {
        return if (state is State.Normal) {
            val hpReduceByRad = Math.pow(state.radiationLevel, 2.0) * radiationHpCoff
            val hpRegen = hpRegen
            val newHp = state.healthPoints - hpReduceByRad + hpRegen
            state.calculateStateByHp(newHp)
        } else {
            state
        }
    }
}

class Graveyard : Effect {
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

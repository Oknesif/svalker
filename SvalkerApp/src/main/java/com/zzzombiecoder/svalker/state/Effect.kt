package com.zzzombiecoder.svalker.state

interface Effect {
    fun apply(state: State): State
}


private fun State.Normal.calculateStateByHp(
        hp: Double,
        causeOfDeath: CauseOfDeath
): State {
    return when {
        hp <= 0 -> State.Dead(cause = causeOfDeath)
        hp > MAX_HEALTH -> this.copy(healthPoints = MAX_HEALTH)
        else -> this.copy(healthPoints = hp)
    }
}

class ReviveEffect : Effect {
    override fun apply(state: State): State {
        return State.Normal()
    }

}

class DeathEffect : Effect {
    override fun apply(state: State): State {
        return State.Dead(cause = CauseOfDeath.SUICIDE)
    }
}

class ReduceHealthEffect(private val reducingPoints: Double) : Effect {
    override fun apply(state: State): State {
        return if (state is State.Normal) {
            state.calculateStateByHp(
                    hp = state.healthPoints - reducingPoints,
                    causeOfDeath = CauseOfDeath.ANOMALY)
        } else {
            state
        }
    }
}

class RadiationSpotEffect(private val addedPoints: Double) : Effect {

    override fun apply(state: State): State {
        return if (state is State.Normal) {
            val newRadiationLevel = state.radiationLevel + addedPoints
            val radiation = if (newRadiationLevel > MAX_RADIATION) {
                MAX_RADIATION
            } else {
                newRadiationLevel
            }
            state.copy(radiationLevel = radiation)
        } else {
            state
        }
    }
}

class LifeEffect : Effect {

    private val radiationHpCoff = 0.3
    private val hpRegen = 0.03

    override fun apply(state: State): State {
        return if (state is State.Normal) {
            val hpReduceByRad = Math.pow(state.radiationLevel, 2.0) * radiationHpCoff
            val hpRegen = hpRegen
            val newHp = state.healthPoints - hpReduceByRad + hpRegen
            state.calculateStateByHp(newHp, CauseOfDeath.RADIATION)
        } else {
            state
        }
    }
}

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

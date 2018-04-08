package com.zzzombiecoder.svalker.state

sealed class State {

    data class Normal(
            val healthPoints: Double = MAX_HEALTH,
            val radiationLevel: Double = 0.0
    ) : State()

    class Zombied : State()

    class Dead(val timeToRespawnSeconds: Long = TIME_IN_GRAVEYARD) : State()
}
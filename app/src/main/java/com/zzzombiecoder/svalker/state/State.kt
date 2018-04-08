package com.zzzombiecoder.svalker.state

sealed class State {

    data class Normal(
            val healthPoints: Double = 100.0,
            val radiationLevel: Double = 0.0
    ) : State()

    class Zombied() : State()

    class Dead(val timeToRespawnSeconds: Long = TIME_IN_GRAVEYARD) : State()
}
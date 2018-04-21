package com.zzzombiecoder.svalker.state

import com.zzzombiecoder.svalker.state.effects.EffectModifier

sealed class State {

    data class NotInGame(
            val savedState: State = State.Normal()
    ) : State()

    data class Normal(
            val health: Double = MAX_HEALTH,
            val psy: Double = MAX_PSI,
            val radiation: Double = 0.0,
            val effectModifiersTime: LongArray = LongArray(EffectModifier.values().size)
    ) : State() {

        fun setEffectModifierTime(modifier: EffectModifier, seconds: Long) {
            effectModifiersTime[modifier.ordinal] = seconds
        }

        fun getEffectModifierTime(modifier: EffectModifier): Long {
            return effectModifiersTime[modifier.ordinal]
        }

    }

    data class Zombie(
            val timeToDeath: Long = TIME_BEING_ZOMBIE
    ) : State()

    data class Dead(
            val timeToRespawnSeconds: Long = TIME_IN_GRAVEYARD,
            val cause: CauseOfDeath
    ) : State()
}
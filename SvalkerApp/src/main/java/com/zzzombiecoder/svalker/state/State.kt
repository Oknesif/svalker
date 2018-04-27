package com.zzzombiecoder.svalker.state

import com.zzzombiecoder.svalker.state.effects.EffectModifier

sealed class State {

    data class NotInGame(
            val savedState: State.Normal = State.Normal()
    ) : State()

    data class Normal(
            val health: Double = MAX_HEALTH,
            val psy: Double = MAX_PSI,
            val radiation: Double = 0.0,
            val effectModifiersLastSeconds: LongArray = LongArray(EffectModifier.values().size)
    ) : State() {

        fun setEffectModifierTime(modifier: EffectModifier, seconds: Long) {
            effectModifiersLastSeconds[modifier.ordinal] = seconds
        }

        fun getEffectModifierTime(modifier: EffectModifier): Long {
            return effectModifiersLastSeconds[modifier.ordinal]
        }

        fun getActiveModifiers(): List<EffectModifier> {
            val list = mutableListOf<EffectModifier>()
            for (i in 0 until EffectModifier.values().size) {
                if (effectModifiersLastSeconds[i] > 0) {
                    list.add(EffectModifier.values()[i])
                }
            }
            return list
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
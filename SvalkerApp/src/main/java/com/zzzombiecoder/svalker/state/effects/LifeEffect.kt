package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.CauseOfDeath
import com.zzzombiecoder.svalker.state.MAX_HEALTH
import com.zzzombiecoder.svalker.state.MAX_PSI
import com.zzzombiecoder.svalker.state.State

class LifeEffect : Effect {

    private val radiationHpCoefficient = 0.2
    private val hpRegen = 0.03
    private val psyRegen = 0.12
    private val radiationReduce = 0.06

    override fun apply(state: State): State {
        return when (state) {
            is State.Normal -> {
                state.advanceTimeForModifiers()

                var newRadiation = state.radiation - state.calculateRadiationReduce()
                newRadiation = if (newRadiation < 0) 0.0 else newRadiation
                val hpReduceByRad = newRadiation * radiationHpCoefficient
                val hpRegen = state.calculateHpRegen()
                var newHp = state.health - hpReduceByRad + hpRegen
                newHp = if (newHp > MAX_HEALTH) MAX_HEALTH else newHp

                val psyRegen = psyRegen
                var newPsi = state.psy + psyRegen
                newPsi = if (newPsi > MAX_PSI) MAX_PSI else newPsi

                return when {
                    newHp <= 0 -> State.Dead(cause = CauseOfDeath.RADIATION)
                    newPsi <= 0 -> State.Zombie()
                    else -> state.copy(health = newHp, psy = newPsi, radiation = newRadiation)
                }
            }
            is State.NotInGame -> {
                val innerState = state.savedState
                val newState = this@LifeEffect.apply(innerState)
                state.copy(savedState = newState)
            }
            else -> state
        }
    }

    private fun State.Normal.advanceTimeForModifiers() {
        for (i in 0 until EffectModifier.values().size) {
            if (effectModifiersLastSeconds[i] > 0) {
                effectModifiersLastSeconds[i] = effectModifiersLastSeconds[i] - 1
            }
        }
    }

    private fun State.Normal.calculateRadiationReduce(): Double {
        return if (getEffectModifierTime(EffectModifier.VODKA) > 0) {
            radiationReduce * EffectModifier.VODKA.factor
        } else {
            radiationReduce
        }
    }

    private fun State.Normal.calculateHpRegen(): Double {
        return if (getEffectModifierTime(EffectModifier.HEAL) > 0) {
            hpRegen * EffectModifier.HEAL.factor
        } else {
            hpRegen
        }
    }

}
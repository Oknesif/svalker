package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.MAX_HEALTH
import com.zzzombiecoder.svalker.state.State

class PsyControllerEffect : Effect {
    private val psyReduce: Double = 34.0
    private val hpReduce: Double = 34.0

    override fun apply(state: State): State {
        if (state is State.Normal) {
            val coefficient =
                    if (state.getEffectModifierTime(EffectModifier.PSY_BLOCK) > 0) 0.5
                    else 1.0
            val minHealth = MAX_HEALTH * 0.3
            var newHp = state.health
            if (newHp > minHealth) {
                newHp = state.health - (hpReduce * coefficient)
                if (newHp < minHealth) {
                    newHp = minHealth
                }
            }

            val newPsy = state.psy - (psyReduce * coefficient)
            return if (newPsy <= 0) {
                State.Zombie()
            } else {
                state.copy(health = newHp, psy = newPsy)
            }
        }
        return state
    }
}
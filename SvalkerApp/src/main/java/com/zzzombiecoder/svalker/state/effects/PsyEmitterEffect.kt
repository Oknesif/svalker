package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.MAX_HEALTH
import com.zzzombiecoder.svalker.state.State

class PsyEmitterEffect : Effect {
    private val psyReduce: Double = 5.0
    private val hpReduce: Double = 5.0

    override fun apply(state: State): State {
        if (state is State.Normal) {
            if (state.getEffectModifierTime(EffectModifier.PSY_BLOCK) > 0) {
                var newHp = state.health - hpReduce
                if (newHp < MAX_HEALTH * 0.3) {
                    newHp = MAX_HEALTH * 0.3
                }
                val newPsy = state.psy - psyReduce
                return if (newPsy <= 0) {
                    State.Zombie()
                } else {
                    state.copy(health = newHp, psy = newPsy)
                }
            }
        }
        return state
    }
}
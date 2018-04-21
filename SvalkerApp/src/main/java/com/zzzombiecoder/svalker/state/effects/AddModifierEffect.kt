package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.State

class AddModifierEffect(
        private val modifier: EffectModifier,
        private val seconds: Long) : Effect {
    override fun apply(state: State): State {
        if (state is State.Normal) {
            state.setEffectModifierTime(modifier, seconds)
        }
        return state
    }
}
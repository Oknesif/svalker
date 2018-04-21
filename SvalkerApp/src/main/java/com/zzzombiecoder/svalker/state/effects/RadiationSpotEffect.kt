package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.MAX_RADIATION
import com.zzzombiecoder.svalker.state.State

class RadiationSpotEffect(private val addedPoints: Double) : Effect {

    override fun apply(state: State): State {
        return if (state is State.Normal) {
            val modifier: Double = if (state.getEffectModifierTime(EffectModifier.ANTI_RAD) > 0) 0.6 else 1.0
            val newRadiationLevel = state.radiation + (addedPoints * modifier)
            val radiation = if (newRadiationLevel > MAX_RADIATION) {
                MAX_RADIATION
            } else {
                newRadiationLevel
            }
            state.copy(radiation = radiation)
        } else {
            state
        }
    }
}
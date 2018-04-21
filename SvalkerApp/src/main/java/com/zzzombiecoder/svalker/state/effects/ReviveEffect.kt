package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.State

class ReviveEffect : Effect {
    override fun apply(state: State): State {
        return State.Normal()
    }

}
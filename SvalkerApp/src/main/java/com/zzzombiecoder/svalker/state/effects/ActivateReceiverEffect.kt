package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.State

class ActivateReceiverEffect : Effect {

    override fun apply(state: State): State {
        if (state is State.NotInGame) {
            return state.savedState
        }
        return state
    }

}
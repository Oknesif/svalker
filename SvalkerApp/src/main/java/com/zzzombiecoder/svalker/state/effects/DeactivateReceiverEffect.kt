package com.zzzombiecoder.svalker.state.effects

import com.zzzombiecoder.svalker.state.State

class DeactivateReceiverEffect: Effect {

    override fun apply(state: State): State {
        return state as? State.NotInGame ?: State.NotInGame(state)
    }

}
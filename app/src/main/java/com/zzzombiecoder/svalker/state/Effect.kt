package com.zzzombiecoder.svalker.state

sealed class Effect {
    class Normal() : Effect()

    class Zombied() : Effect()

    class Dead(): Effect()
}
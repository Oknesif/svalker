package com.zzzombiecoder.svalker.state

fun Signal.toEffect(): Effect {
    return when (this) {
        Signal.Electra -> {
            Electra()
        }
        Signal.Radiation -> {
            RadiationSpot()
        }
        Signal.Graveyard -> {
            Graveyard()
        }
        Signal.None -> {
            None()
        }
    }
}
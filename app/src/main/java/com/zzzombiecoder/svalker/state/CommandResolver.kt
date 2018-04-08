package com.zzzombiecoder.svalker.state

fun Command.toEffect(): Effect {
    return when (this) {
        Command.REVIVE -> {
            Revive()
        }
        Command.DIE -> {
            Death()
        }
    }
}
package com.zzzombiecoder.svalker.state

fun Command.toEffect(): Effect {
    return when (this) {
        Command.REVIVE -> {
            ReviveEffect()
        }
        Command.DIE -> {
            DeathEffect()
        }
    }
}
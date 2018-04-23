package com.zzzombiecoder.svalker.state

import com.zzzombiecoder.code.generator.Code

enum class Command {
    DIE,
    REVIVE,
    HEAL,
    VODKA,
    ANTI_RAD,
    PSY_BLOCK,
    ACTIVATE_EMITTER,
    DEACTIVATE_EMITTER,
}

fun Code.toCommand(): Command {
    return when (this) {
        Code.HEAL -> Command.HEAL
        Code.VODKA -> Command.VODKA
        Code.ANTI_RAD -> Command.ANTI_RAD
        Code.PSY_BLOCK -> Command.PSY_BLOCK
        Code.ACTIVATE_RECEIVER -> Command.ACTIVATE_EMITTER
        Code.DEACTIVATE_RECEIVER -> Command.DEACTIVATE_EMITTER
    }
}
package com.zzzombiecoder.svalker.state

import com.zzzombiecoder.svalker.state.effects.*
import java.util.*
import java.util.concurrent.TimeUnit

fun Command.toEffect(): Effect {
    return when (this) {
        Command.REVIVE -> ReviveEffect()
        Command.DIE -> DeathEffect(causeOfDeath = CauseOfDeath.SUICIDE)
        Command.HEAL -> AddModifierEffect(EffectModifier.HEAL, TimeUnit.MINUTES.toSeconds(20))
        Command.VODKA -> AddModifierEffect(EffectModifier.VODKA, TimeUnit.MINUTES.toSeconds(12))
        Command.ANTI_RAD -> AddModifierEffect(EffectModifier.ANTI_RAD, TimeUnit.MINUTES.toSeconds(20))
        Command.PSY_BLOCK -> {
            val randomTime = (Random().nextInt(4) + 6).toLong()
            val time = TimeUnit.MINUTES.toSeconds(randomTime)
            AddModifierEffect(EffectModifier.PSY_BLOCK, time)
        }
        Command.DEACTIVATE_EMITTER -> DeactivateReceiverEffect()
        Command.ACTIVATE_EMITTER -> ActivateReceiverEffect()
    }
}
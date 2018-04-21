package com.zzzombiecoder.svalker.state.effects.sequences

import android.os.SystemClock
import com.zzzombiecoder.svalker.state.effects.Effect
import java.util.concurrent.TimeUnit

class EffectSequence(
        private val periods: LongArray,
        private val effects: Array<Effect>
) : IEffectSequence {

    constructor(effect: Effect, period: Long, timeUnit: TimeUnit) : this(
            periods = longArrayOf(TimeUnit.MILLISECONDS.convert(period, timeUnit)),
            effects = arrayOf(effect)
    )

    init {
        if (periods.size != effects.size) {
            throw IllegalArgumentException("Size of array have to be the same")
        }
    }

    private var tier: Int = 0
    private var recodedTimeStamp: Long = SystemClock.uptimeMillis()
    private var nextTierTime: Long = recodedTimeStamp + periods[tier]

    override fun registerSignal(): Effect? {
        val timeStamp = SystemClock.uptimeMillis()
        var effect: Effect? = null
        if (timeStamp >= nextTierTime) {
            effect = effects[tier]
            recodedTimeStamp = timeStamp
            switchTier()
        }
        return effect
    }

    private fun switchTier() {
        if (tier == (periods.size - 1)) {
            tier = 0
        } else {
            tier++
        }
        nextTierTime = recodedTimeStamp + periods[tier]
    }
}
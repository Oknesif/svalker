package com.zzzombiecoder.svalker.state

class FrequencyRange(
        private val min: Double,
        private val max: Double
) {
    constructor(frequency: Double) : this(frequency - SIGNAL_FAULT, frequency + SIGNAL_FAULT)

    fun isIn(frequency: Double): Boolean {
        return frequency in min..max
    }
}
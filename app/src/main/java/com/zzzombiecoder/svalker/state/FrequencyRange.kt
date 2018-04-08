package com.zzzombiecoder.svalker.state

class FrequencyRange(
        private val min: Double,
        private val max: Double) {

    constructor(frequency: Double) : this(frequency - FAULT, frequency + FAULT)

    fun isIn(frequency: Double): Boolean {
        return frequency in min..max
    }
}

private const val FAULT = 10.0
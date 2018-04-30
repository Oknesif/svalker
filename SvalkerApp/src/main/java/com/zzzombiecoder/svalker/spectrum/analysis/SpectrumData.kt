package com.zzzombiecoder.svalker.spectrum.analysis

import java.util.*

data class SpectrumData(
        val amplitudeArray: DoubleArray,
        val maxAmpFreq: Double) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpectrumData

        if (!Arrays.equals(amplitudeArray, other.amplitudeArray)) return false
        if (maxAmpFreq != other.maxAmpFreq) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(amplitudeArray)
        result = 31 * result + maxAmpFreq.hashCode()
        return result
    }
}
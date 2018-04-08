package com.zzzombiecoder.svalker.state

import java.util.concurrent.TimeUnit

enum class Signal(
        val frequencyRange: FrequencyRange,
        val timePeriodMls: Long
) {
    Graveyard(
            frequencyRange = FrequencyRange(220.0),
            timePeriodMls = TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS)
    ),
    Radiation(
            frequencyRange = FrequencyRange(134.0),
            timePeriodMls = TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS)
    ),
    //It is important that None is in the end
    None(
            frequencyRange = FrequencyRange(0.0, 3000.0),
            timePeriodMls = 1L
    )
}
package com.zzzombiecoder.svalker.state

import java.util.concurrent.TimeUnit

const val SIGNAL_FAULT = 5.0
const val MIN_DB: Double = -40.0
const val MAX_HEALTH = 100.0
const val MAX_RADIATION = 100.0
val RADIATION_COEFF = 0.2
val TIME_IN_GRAVEYARD = TimeUnit.SECONDS.convert(1, TimeUnit.HOURS)
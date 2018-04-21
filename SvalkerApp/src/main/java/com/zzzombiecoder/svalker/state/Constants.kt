package com.zzzombiecoder.svalker.state

import java.util.concurrent.TimeUnit

const val SIGNAL_FAULT = 5.0
const val MIN_DB: Double = -40.0
const val MAX_HEALTH = 100.0
const val MAX_PSI = 100.0
const val MAX_RADIATION = 100.0

val TIME_IN_GRAVEYARD = TimeUnit.SECONDS.convert(3, TimeUnit.HOURS)
val TIME_BEING_ZOMBIE = TimeUnit.SECONDS.convert(1, TimeUnit.HOURS)
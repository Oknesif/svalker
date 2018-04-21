package com.zzzombiecoder.svalker.service

import com.zzzombiecoder.svalker.state.Command
import com.zzzombiecoder.svalker.state.SignalType
import com.zzzombiecoder.svalker.state.State
import io.reactivex.Observable

interface ServiceBinder {

    fun getStateUpdates(): Observable<State>

    fun getSignal(): Observable<SignalType>

    fun setCommandSource(commandSource: Observable<Command>)
}
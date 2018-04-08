package com.zzzombiecoder.svalker.service

import com.zzzombiecoder.svalker.spectrum.analysis.SpectrumData
import com.zzzombiecoder.svalker.state.Command
import com.zzzombiecoder.svalker.state.Signal
import com.zzzombiecoder.svalker.state.State
import io.reactivex.Observable

interface ServiceBinder {

    fun getStateUpdates(): Observable<State>

    fun getSpectrumData(): Observable<SpectrumData>

    fun getSignal(): Observable<Signal>

    fun setCommandSource(commandSource: Observable<Command>)
}
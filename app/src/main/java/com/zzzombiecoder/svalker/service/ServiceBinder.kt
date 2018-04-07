package com.zzzombiecoder.svalker.service

import com.zzzombiecoder.svalker.spectrum.analysis.SpectrumData
import com.zzzombiecoder.svalker.state.State
import io.reactivex.Observable

interface ServiceBinder {

    fun getStateUpdates(): Observable<State>

    fun getSpectrumData(): Observable<SpectrumData>
}
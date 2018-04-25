package com.zzzombiecoder.svalker.views

import com.zzzombiecoder.svalker.state.SignalType
import com.zzzombiecoder.svalker.state.State

interface IMainView {

    fun updateUserState(state: State)

    fun updateSignalInfo(signal: SignalType)

}
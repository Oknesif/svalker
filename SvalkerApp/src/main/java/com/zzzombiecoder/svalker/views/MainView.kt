package com.zzzombiecoder.svalker.views

import android.app.Activity
import android.graphics.Color
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.zzzombiecoder.svalker.R
import com.zzzombiecoder.svalker.state.SignalType
import com.zzzombiecoder.svalker.state.State

interface IMainView {

    fun updateUserState(state: State)

    fun updateSignalInfo(signal: SignalType)

}

class MainView(
        private val activity: Activity
) : IMainView {

    private val stateView: TextView = activity.findViewById(R.id.state_txt)
    private val signalView: TextView = activity.findViewById(R.id.signal_txt)

    override fun updateUserState(state: State) {
        val graveyardTime = activity.getString(R.string.graveyard_time)
        val stateTitle = activity.getString(R.string.state)
        val healthTitle = activity.getString(R.string.health)
        val radiationTitle = activity.getString(R.string.radiation)
        val stateString = when (state) {
            is State.NotInGame -> activity.getString(R.string.not_in_game)
            is State.Normal -> activity.getString(R.string.normal)
            is State.Dead -> activity.getString(R.string.dead)
            is State.Zombie -> activity.getString(R.string.zombie)
        }
        stateView.text = buildSpannedString {
            bold { append("$stateTitle: ") }
            append(stateString)
            append("\n")
            if (state is State.Normal) {
                bold { append("$healthTitle: ") }
                append(state.health.toInt().toString())
                append("\n")
                bold { append("$radiationTitle: ") }
                append(state.radiation.toString())
                append("\n")
            }
            if (state is State.Dead) {
                bold { append(graveyardTime) }
                append("\n")
                append("Осталось секунд: ${state.timeToRespawnSeconds}")
            }
        }

    }


    override fun updateSignalInfo(signal: SignalType) {
        val signalTitle = activity.getString(R.string.last_received_signal)
        val signalColor = when (signal) {
            SignalType.None -> Color.WHITE
            SignalType.Radiation1 -> Color.YELLOW
            SignalType.Radiation2 -> Color.YELLOW
            SignalType.Radiation3 -> Color.YELLOW
            SignalType.Radiation4 -> Color.YELLOW
            SignalType.Radiation5 -> Color.YELLOW
            SignalType.Graveyard -> Color.GREEN
            SignalType.Electra -> Color.BLUE
            SignalType.Studen -> Color.GREEN
            SignalType.Inferno -> Color.RED
            SignalType.psy_emmiter -> Color.CYAN
            SignalType.psy_controller -> Color.CYAN
        }
        val signalText = when (signal) {
            SignalType.None -> activity.getString(R.string.none)
            SignalType.Radiation1 -> activity.getString(R.string.radiation1)
            SignalType.Radiation2 -> activity.getString(R.string.radiation2)
            SignalType.Radiation3 -> activity.getString(R.string.radiation3)
            SignalType.Radiation4 -> activity.getString(R.string.radiation4)
            SignalType.Radiation5 -> activity.getString(R.string.radiation5)
            SignalType.Graveyard -> activity.getString(R.string.graveyard)
            SignalType.Electra -> activity.getString(R.string.electra)
            SignalType.Studen -> "студень"
            SignalType.Inferno -> "жарка"
            SignalType.psy_emmiter -> "пси излучатель"
            SignalType.psy_controller -> "контроллер"
        }
        signalView.text = buildSpannedString {
            append(signalTitle)
            append("\n")
            bold { color(signalColor) { append(signalText) } }
        }
    }

}
package com.zzzombiecoder.svalker.views

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.transition.TransitionManager
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.zzzombiecoder.svalker.R
import com.zzzombiecoder.svalker.state.SignalType
import com.zzzombiecoder.svalker.state.State
import com.zzzombiecoder.svalker.utils.setOnFlingListener

class MainView(
        private val activity: Activity
) : IMainView {

    private val mainLayout: ConstraintLayout = activity.findViewById(R.id.main_layout)

    private val healthBarView: BarView = activity.findViewById(R.id.health_bar_view)
    private val psyBarView: BarView = activity.findViewById(R.id.psy_bar_view)
    private val arrowView: ArrowView = activity.findViewById(R.id.arrow_view)

    private val bottomPanelView: View = activity.findViewById(R.id.bottom_panel_view)
    private val effectView: View = activity.findViewById(R.id.effect_view)

    private val stateView: TextView = activity.findViewById(R.id.state_txt)
    private val signalView: TextView = activity.findViewById(R.id.signal_txt)

    init {
        effectView.setOnFlingListener { _, velocityY ->
            changeBottomPanelVisibility(velocityY < 0)
        }
    }

    override fun updateUserState(state: State) {
        when (state) {
            is State.Normal -> {
                healthBarView.setValue(state.health)
                psyBarView.setValue(state.psy)
            }
            is State.NotInGame -> {
                updateUserState(state.savedState)
            }
        }

        val graveyardTime = activity.getString(R.string.graveyard_time)
        val stateTitle = activity.getString(R.string.state)
        val healthTitle = activity.getString(R.string.health)
        val radiationTitle = activity.getString(R.string.radiation)
        val psyHealthTitle = activity.getString(R.string.psy_health)
        val effectsTitle = activity.getString(R.string.effect_modifiers)
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
                bold { append("$psyHealthTitle: ") }
                append(state.psy.toString())
                append("\n")

                val modifiers = state.getActiveModifiers()
                if (modifiers.isEmpty().not()) {
                    bold { append(effectsTitle) }
                    append(modifiers.joinToString { "$it, " })
                    append("\n")
                }
            }
            if (state is State.Dead) {
                bold { append(graveyardTime) }
                append("\n")
                append("Осталось секунд: ${state.timeToRespawnSeconds}")
            }
        }
    }

    override fun updateSignalInfo(signal: SignalType) {
        playEffectAnimation(signal)
        setGeigerCounterValue(signal)

        val signalTitle = activity.getString(R.string.last_received_signal)
        val signalColor = when (signal) {
            SignalType.None -> Color.WHITE
            SignalType.Unplugged -> Color.RED
            SignalType.Radiation1 -> Color.YELLOW
            SignalType.Radiation2 -> Color.YELLOW
            SignalType.Radiation3 -> Color.YELLOW
            SignalType.Radiation4 -> Color.YELLOW
            SignalType.Radiation5 -> Color.YELLOW
            SignalType.Graveyard -> Color.GREEN
            SignalType.Electra -> Color.BLUE
            SignalType.Studen -> Color.GREEN
            SignalType.Inferno -> Color.RED
            SignalType.Psy_emmiter -> Color.CYAN
            SignalType.Psy_controller -> Color.CYAN
        }
        val signalText = when (signal) {
            SignalType.Unplugged -> activity.getString(R.string.unplugged)
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
            SignalType.Psy_emmiter -> "пси излучатель"
            SignalType.Psy_controller -> "контроллер"
        }
        signalView.text = buildSpannedString {
            append(signalTitle)
            append("\n")
            bold { color(signalColor) { append(signalText) } }
        }
    }

    private fun setGeigerCounterValue(signal: SignalType) {
        val targetValue = when (signal) {
            SignalType.Radiation1 -> 1
            SignalType.Radiation2 -> 2
            SignalType.Radiation3 -> 3
            SignalType.Radiation4 -> 4
            SignalType.Radiation5 -> 5
            else -> 0
        }
        arrowView.setTargetValue(targetValue)
    }

    private fun playEffectAnimation(signal: SignalType) {
        if (signal == SignalType.None) {
            return
        }
        val drawable = getDrawableForEffect(signal)
        if (drawable != null) {
            effectView.background = drawable
            val animation = effectView.animation ?: AlphaAnimation(0f, 1f).apply {
                duration = 1000
                repeatCount = 1
                repeatMode = Animation.REVERSE
                isFillEnabled = true
                fillAfter = true
            }
            if (animation.hasStarted().not() || animation.hasEnded()) {
                effectView.startAnimation(animation)
            }
        }
    }

    private fun getDrawableForEffect(signal: SignalType): Drawable? {
        val drawableId = when (signal) {
            SignalType.Electra -> R.drawable.light
            SignalType.Inferno -> R.drawable.heat
            SignalType.Psy_controller, SignalType.Psy_emmiter -> R.drawable.mind
            SignalType.Studen -> R.drawable.slime
            else -> return null
        }
        return ContextCompat.getDrawable(activity, drawableId)
    }

    private fun changeBottomPanelVisibility(visible: Boolean) {
        val params = bottomPanelView.layoutParams as ConstraintLayout.LayoutParams
        params.bottomToBottom = if (visible) {
            ConstraintLayout.LayoutParams.PARENT_ID
        } else {
            ConstraintLayout.LayoutParams.UNSET
        }
        params.topToBottom = if (visible) {
            ConstraintLayout.LayoutParams.UNSET
        } else {
            ConstraintLayout.LayoutParams.PARENT_ID
        }
        bottomPanelView.layoutParams = params
        TransitionManager.beginDelayedTransition(mainLayout)
    }
}
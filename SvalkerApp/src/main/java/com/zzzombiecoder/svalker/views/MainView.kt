package com.zzzombiecoder.svalker.views

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.scale
import com.zzzombiecoder.svalker.R
import com.zzzombiecoder.svalker.state.SignalType
import com.zzzombiecoder.svalker.state.State
import com.zzzombiecoder.svalker.state.effects.EffectModifier
import java.util.concurrent.TimeUnit

class MainView(
        private val activity: Activity
) : IMainView {
    private val healthBarView: BarView = activity.findViewById(R.id.health_bar_view)
    private val psyBarView: BarView = activity.findViewById(R.id.psy_bar_view)
    private val radiationBar: RadiationBar = activity.findViewById(R.id.radiation_bar_view)
    private val arrowView: ArrowView = activity.findViewById(R.id.arrow_view)
    private val effectView: View = activity.findViewById(R.id.effect_view)

    private val vodkaView: TextView = activity.findViewById(R.id.vodka_view)
    private val healView: TextView = activity.findViewById(R.id.heal_view)
    private val antiRadView: TextView = activity.findViewById(R.id.antirad_view)
    private val antiPsyView: TextView = activity.findViewById(R.id.antipsy_view)

    private val messageView: TextView = activity.findViewById(R.id.message_view)

    override fun updateUserState(state: State) {
        when (state) {
            is State.Normal -> {
                healthBarView.setValue(state.health)
                psyBarView.setValue(state.psy)
                radiationBar.setValue(state.radiation)
            }
            is State.Dead -> {
                healthBarView.setValue(0.0)
            }
            is State.Zombie -> {
                psyBarView.setValue(0.0)
            }
            is State.NotInGame -> {
                updateUserState(state.savedState)
            }
        }
        updateEffectModifierPanel(state)
        updateMessageView(state)
    }

    override fun updateSignalInfo(signal: SignalType) {
        if (messageView.visibility == View.VISIBLE) {
            arrowView.stop()
        } else {
            playEffectAnimation(signal)
            setGeigerCounterValue(signal)
        }
    }

    private fun updateMessageView(state: State) {
        messageView.visibility = if (state is State.Normal) View.GONE else View.VISIBLE

        val (title, description, time) = when (state) {
            is State.NotInGame -> {
                Triple(
                        activity.getString(R.string.not_in_zone),
                        activity.getString(R.string.not_in_zone_description),
                        null)
            }
            is State.Dead -> {
                val hours = TimeUnit.SECONDS.toHours(state.timeToRespawnSeconds)
                Triple(
                        activity.getString(R.string.dead),
                        if (hours < 2) {
                            activity.getString(R.string.dead_description_additional)
                        } else {
                            activity.getString(R.string.dead_description)
                        },
                        state.timeToRespawnSeconds.toTimeFormat()
                )
            }
            is State.Zombie -> {
                Triple(
                        activity.getString(R.string.zombie),
                        activity.getString(R.string.zombie_description),
                        state.timeToDeath.toTimeFormat()
                )
            }
            is State.Normal -> {
                Triple("", "", null)
            }
        }
        val text = buildSpannedString {
            scale(2f) { append("$title\n\n") }
            append("$description\n\n")
            if (time != null) {
                scale(3f) { append("$time") }
            }
        }
        messageView.text = text
    }

    private fun Long.toTimeFormat(): String {
        val hours = TimeUnit.SECONDS.toHours(this)
        val minutes = TimeUnit.SECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = this - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours)
        return "%02d:%02d:%02d".format(hours, minutes, seconds)
    }

    private fun updateEffectModifierPanel(state: State) {
        if (state is State.Normal) {
            vodkaView.setActiveModifier(state.getEffectModifierTime(EffectModifier.VODKA))
            healView.setActiveModifier(state.getEffectModifierTime(EffectModifier.HEAL))
            antiRadView.setActiveModifier(state.getEffectModifierTime(EffectModifier.ANTI_RAD))
            antiPsyView.setActiveModifier(state.getEffectModifierTime(EffectModifier.PSY_BLOCK))
        } else {
            vodkaView.setActiveModifier(0)
            healView.setActiveModifier(0)
            antiRadView.setActiveModifier(0)
            antiPsyView.setActiveModifier(0)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun TextView.setActiveModifier(secondsLeft: Long) {
        if (secondsLeft > 0) {
            val minutes = TimeUnit.SECONDS.toMinutes(secondsLeft)
            val seconds = secondsLeft - TimeUnit.MINUTES.toSeconds(minutes)
            this.isSelected = true
            this.text = "%02d:%02d".format(minutes, seconds)
        } else {
            this.isSelected = false
            this.text = ""
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
            SignalType.PsyController, SignalType.PsyEmmiter -> R.drawable.mind
            SignalType.Studen -> R.drawable.slime
            SignalType.Unplugged -> R.drawable.unplugged
            else -> return null
        }
        return ContextCompat.getDrawable(activity, drawableId)
    }
}
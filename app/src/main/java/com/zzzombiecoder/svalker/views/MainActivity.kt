package com.zzzombiecoder.svalker.views

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zzzombiecoder.svalker.R
import com.zzzombiecoder.svalker.service.ServiceBinder
import com.zzzombiecoder.svalker.service.SvalkerService
import com.zzzombiecoder.svalker.state.Command
import com.zzzombiecoder.svalker.state.Signal
import com.zzzombiecoder.svalker.state.State
import com.zzzombiecoder.svalker.utils.plusAssign
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class MainActivity : AppCompatActivity() {

    private lateinit var stateView: TextView
    private lateinit var signalView: TextView
    private lateinit var stopServiceButton: View
    private lateinit var dieButton: View
    private lateinit var reviveButton: View
    private lateinit var disposable: CompositeDisposable
    private val commandSubject: Subject<Command> = PublishSubject.create()

    private val serviceIntent: Intent by lazy { Intent(this, SvalkerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stateView = findViewById(R.id.state_txt)
        signalView = findViewById(R.id.signal_txt)
        stopServiceButton = findViewById(R.id.stop_service_button)
        stopServiceButton.setOnClickListener { stopService(serviceIntent) }
        findViewById<View>(R.id.die_button).setOnClickListener { commandSubject.onNext(Command.DIE) }
        findViewById<View>(R.id.revive_button).setOnClickListener { commandSubject.onNext(Command.REVIVE) }
    }

    override fun onStart() {
        super.onStart()
        disposable = CompositeDisposable()
        disposable += RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe({
                    if (it) {
                        startService(serviceIntent)
                        bindService(serviceIntent, serviceConnection, 0)
                    }
                }, {
                    Log.d("MainActivity", Log.getStackTraceString(it))
                })
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        disposable.dispose()
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            finish()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            disposable += (service as ServiceBinder)
                    .getSignal()
                    .startWith(Signal.None)
                    .subscribe {
                        updateSignalInfo(it)
                    }
            disposable += (service as ServiceBinder)
                    .getStateUpdates()
                    .subscribe {
                        updateUserState(it)
                    }

            (service as ServiceBinder).setCommandSource(commandSubject)
        }
    }

    private fun updateSignalInfo(signal: Signal) {
        val signalTitle = getString(R.string.last_received_signal)
        val signalColor = when (signal) {
            Signal.None -> Color.WHITE
            Signal.Radiation -> Color.YELLOW
            Signal.Graveyard -> Color.GREEN
            Signal.Electra -> Color.BLUE
        }
        val signalText = when (signal) {
            Signal.None -> getString(R.string.none)
            Signal.Radiation -> getString(R.string.radiation)
            Signal.Graveyard -> getString(R.string.graveyard)
            Signal.Electra -> getString(R.string.electra)
        }
        signalView.text = buildSpannedString {
            append(signalTitle)
            append("\n")
            bold { color(signalColor) { append(signalText) } }
        }
    }

    private fun updateUserState(state: State) {
        val graveyardTime = getString(R.string.graveyard_time)
        val stateTitle = getString(R.string.state)
        val healthTitle = getString(R.string.health)
        val radiationTitle = getString(R.string.radiation)
        val stateString = when (state) {
            is State.Normal -> getString(R.string.normal)
            is State.Dead -> getString(R.string.dead)
            is State.Zombied -> getString(R.string.zombie)
        }
        stateView.text = buildSpannedString {
            bold { append("$stateTitle: ") }
            append(stateString)
            append("\n")
            if (state is State.Normal) {
                bold { append("$healthTitle: ") }
                append(state.healthPoints.toInt().toString())
                append("\n")
                bold { append("$radiationTitle: ") }
                append(state.radiationLevel.toString())
                append("\n")
            }
            if (state is State.Dead) {
                bold { append(graveyardTime) }
                append("\n")
                append("Осталось секунд: ${state.timeToRespawnSeconds}")
            }
        }

    }
}

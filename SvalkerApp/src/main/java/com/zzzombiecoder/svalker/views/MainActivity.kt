package com.zzzombiecoder.svalker.views

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.core.widget.toast
import com.google.zxing.integration.android.IntentIntegrator
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zzzombiecoder.code.generator.getCode
import com.zzzombiecoder.svalker.R
import com.zzzombiecoder.svalker.service.ServiceBinder
import com.zzzombiecoder.svalker.service.SvalkerService
import com.zzzombiecoder.svalker.state.Command
import com.zzzombiecoder.svalker.state.SignalType
import com.zzzombiecoder.svalker.state.toCommand
import com.zzzombiecoder.svalker.utils.plusAssign
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var mainView: IMainView
    private lateinit var disposable: CompositeDisposable

    private val commandSubject: Subject<Command> = PublishSubject.create()

    private val serviceIntent: Intent by lazy { Intent(this, SvalkerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)
        mainView = MainView(this)
        setClickListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val codeString = result.contents
                val code = codeString.getCode()
                if (code == null) {
                    this.toast("incorrect")
                } else {
                    commandSubject.onNext(code.toCommand())
                }
            } else {
                toast("canceled")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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
                    } else {
                        finish()
                    }
                }, {
                    Timber.e(it)
                })
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        disposable.dispose()
    }

    private fun setClickListeners() {
        findViewById<View>(R.id.stop_service_button).setOnClickListener { stopService(serviceIntent) }
        findViewById<View>(R.id.die_button).setOnClickListener { commandSubject.onNext(Command.DIE) }
        findViewById<View>(R.id.revive_button).setOnClickListener { commandSubject.onNext(Command.REVIVE) }
        findViewById<View>(R.id.scanner_button).setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt(getString(R.string.message_to_scan))
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            finish()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            disposable += (service as ServiceBinder)
                    .getSignal()
                    .startWith(SignalType.None)
                    .subscribe {
                        mainView.updateSignalInfo(it)
                    }
            disposable += (service as ServiceBinder)
                    .getStateUpdates()
                    .subscribe {
                        mainView.updateUserState(it)
                    }

            (service as ServiceBinder).setCommandSource(commandSubject)
        }
    }
}

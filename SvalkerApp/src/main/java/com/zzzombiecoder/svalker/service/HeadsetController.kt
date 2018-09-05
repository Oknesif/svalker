package com.zzzombiecoder.svalker.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


class HeadsetController(
        private val context: Context
) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun stopBluetoothSco(){
        audioManager.stopBluetoothSco()
    }

    fun startBluetoothSco(): Completable {
        return Completable.create { emitter ->
            val intentFilter = IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED)
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1)
                    if (AudioManager.SCO_AUDIO_STATE_CONNECTED == state) {
                        emitter.onComplete()
                    }
                }
            }
            audioManager.startBluetoothSco()
            context.registerReceiver(receiver, intentFilter)
            emitter.setCancellable { context.unregisterReceiver(receiver) }
        }
    }

    fun isBluetoothHeadsetConnected(): Observable<Boolean> {
        return Observable
                .interval(1, TimeUnit.SECONDS)
                .map { audioManager.isBluetoothA2dpOn }
    }
}
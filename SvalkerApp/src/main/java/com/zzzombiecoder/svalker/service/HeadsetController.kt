package com.zzzombiecoder.svalker.service

import android.content.Context
import android.media.AudioManager
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


class HeadsetController(
        private val context: Context
) {

    fun isWiredHeadsetOn(): Observable<Boolean> {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return Observable.interval(1, TimeUnit.SECONDS)
                .map { audioManager.isBluetoothA2dpOn }
    }
}
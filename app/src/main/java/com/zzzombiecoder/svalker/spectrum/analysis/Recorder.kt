package com.zzzombiecoder.svalker.spectrum.analysis

import android.media.AudioFormat
import android.media.AudioRecord
import com.zzzombiecoder.svalker.spectrum.analysis.AnalyzerParameters
import com.zzzombiecoder.svalker.spectrum.analysis.STFT
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class Recorder {
    private val analyzerParam = AnalyzerParameters()
    private val stft = STFT(analyzerParam)
    private val readChunkSize = Math.min(analyzerParam.hopLen, 2048)
    private var record: AudioRecord? = null

    private fun createRecorder(): AudioRecord {
        val minBytes = AudioRecord.getMinBufferSize(
                analyzerParam.sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT)

        val bufferSampleSize = Math.max(
                minBytes / analyzerParam.BYTE_OF_SAMPLE, analyzerParam.fftLen / 2) * 2
        return AudioRecord(
                analyzerParam.audioSourceId,
                analyzerParam.sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                analyzerParam.BYTE_OF_SAMPLE * bufferSampleSize)

    }

    fun getSpectrumAmpDB(mSec: Long): Observable<DoubleArray> =
            Observable.interval(mSec, TimeUnit.MILLISECONDS)
                    .doOnSubscribe {
                        record = createRecorder()
                        record?.startRecording()
                    }
                    .doOnDispose {
                        record?.stop()
                        record?.release()
                        record = null
                    }
                    .doOnNext {
                        val audioSamples = ShortArray(readChunkSize)
                        val numOfReadShort = record?.read(audioSamples, 0, readChunkSize) ?: 0
                        stft.feedData(audioSamples, numOfReadShort)
                    }
                    .filter { stft.nElemSpectrumAmp() >= analyzerParam.nFFTAverage }
                    .map {
                        stft.spectrumAmpDB
                    }
                    .share()
}
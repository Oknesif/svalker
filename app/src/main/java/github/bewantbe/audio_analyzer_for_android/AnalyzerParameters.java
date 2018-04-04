/* Copyright 2017 Eddy Xiao <bewantbe@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.bewantbe.audio_analyzer_for_android;

import android.media.MediaRecorder;

/**
 * Basic properties of Analyzer.
 */

public class AnalyzerParameters {
    final int RECORDER_AGC_OFF = MediaRecorder.AudioSource.VOICE_RECOGNITION;
    public int audioSourceId = RECORDER_AGC_OFF;
    public int sampleRate = 16000;
    public int fftLen = 2048;
    public int hopLen = 1024;
    double overlapPercent = 50;  // = (1 - hopLen/fftLen) * 100%
    String wndFuncName = "Hanning";
    public int nFFTAverage = 2;
    boolean isAWeighting = false;
    public final int BYTE_OF_SAMPLE = 2;
    final double SAMPLE_VALUE_MAX = 32767.0;   // Maximum signal value
    double spectrogramDuration = 4.0;

    double[] micGainDB = null;  // should have fftLen/2+1 elements, i.e. include DC.
    String calibName = null;

    public AnalyzerParameters() {}

}

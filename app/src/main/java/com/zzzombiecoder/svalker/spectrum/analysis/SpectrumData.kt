package com.zzzombiecoder.svalker.spectrum.analysis

import android.os.Parcel
import android.os.Parcelable
import java.util.*


data class SpectrumData(val amplitudeArray: DoubleArray, val maxFrequency: Double) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.createDoubleArray(),
            parcel.readDouble())
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpectrumData

        if (!Arrays.equals(amplitudeArray, other.amplitudeArray)) return false
        if (maxFrequency != other.maxFrequency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(amplitudeArray)
        result = 31 * result + maxFrequency.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDoubleArray(amplitudeArray)
        parcel.writeDouble(maxFrequency)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SpectrumData> {
        override fun createFromParcel(parcel: Parcel): SpectrumData {
            return SpectrumData(parcel)
        }

        override fun newArray(size: Int): Array<SpectrumData?> {
            return arrayOfNulls(size)
        }
    }
}
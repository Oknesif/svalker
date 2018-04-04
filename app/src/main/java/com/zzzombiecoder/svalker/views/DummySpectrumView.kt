package com.zzzombiecoder.svalker.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class DummySpectrumView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var data: DoubleArray? = null
    private val paint = Paint()

    init {
        paint.isAntiAlias = true
        paint.color = Color.GREEN
    }

    fun feedData(data: DoubleArray) {
        this.data = data
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        data?.let {
            Log.d("ShowMeView", "Length: ${it.size}, max value: ${it.max()} at posdataion ${it.maxPosition()}")
            for (i in 0 until canvas.width) {
                if (i < it.size - 1) {
                    val dotToDraw = it[i]
                    canvas.drawPoint(i.toFloat(), Math.abs(dotToDraw.toFloat()), paint)
                }
            }
        }
    }

    private fun DoubleArray.maxPosition(): Int {
        if (isEmpty()) return -1
        var maxPosition = 0
        var max = this[0]
        if (max.isNaN()) return 0
        for (i in 1..lastIndex) {
            val e = this[i]
            if (e.isNaN()) return i
            if (max < e) {
                max = e
                maxPosition = i
            }
        }
        return maxPosition
    }

}
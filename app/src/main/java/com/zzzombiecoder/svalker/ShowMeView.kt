package com.zzzombiecoder.svalker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ShowMeView @JvmOverloads constructor(
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
            for (i in 0 until canvas.width) {
                if (i < it.size - 1) {
                    val dotToDraw = it[i]
                    canvas.drawPoint(i.toFloat(), Math.abs(dotToDraw.toFloat()), paint)
                }
            }
        }
    }
}
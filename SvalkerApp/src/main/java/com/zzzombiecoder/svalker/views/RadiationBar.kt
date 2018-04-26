package com.zzzombiecoder.svalker.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import com.zzzombiecoder.svalker.R

class RadiationBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var currentValue: Double = 0.0
    private val padding = 4 * resources.displayMetrics.density
    private val paint = Paint()

    init {
        paint.color = ContextCompat.getColor(context, R.color.svalker_yellow)
        paint.style = Paint.Style.FILL
    }

    fun setValue(radiation: Double) {
        val value = radiation / 30
        currentValue = if (value > 1) {
            1.0
        } else {
            value
        }
        ViewCompat.postInvalidateOnAnimation(this)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val left = padding
        val top = padding
        val bottom = height - padding
        val right = (currentValue * (width - 2 * padding) + padding).toFloat()

        canvas.drawRect(left, top, right, bottom, paint)
    }
}
package com.zzzombiecoder.svalker.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import com.zzzombiecoder.svalker.R

class BarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val strokePaint = Paint()
    private val fillPaint = Paint()
    private var currentValue: Double = 1.0
    private val space = 16.toDP()
    private val strokeWidth = 3.toDP()
    private val halfStroke = 3.toDP() / 2f
    private val columnWidth = lazy { (width - (4 * space)) / 5f }

    init {
        strokePaint.isAntiAlias = true
        strokePaint.color = ContextCompat.getColor(context, R.color.svalker_green)
        strokePaint.strokeWidth = strokeWidth
        strokePaint.style = Paint.Style.STROKE

        fillPaint.isAntiAlias = true
        fillPaint.color = ContextCompat.getColor(context, R.color.svalker_green)
        fillPaint.style = Paint.Style.FILL
    }

    private fun Int.toDP(): Float {
        return this * resources.displayMetrics.density
    }

    fun setValue(value: Double) {
        this.currentValue = value / 100.0
        ViewCompat.postInvalidateOnAnimation(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in 0..4) {
            var left = i * (columnWidth.value + space)
            var top = 0f
            var bottom = height.toFloat()
            var right = left + columnWidth.value
            left += halfStroke
            top += halfStroke
            bottom -= halfStroke
            right -= halfStroke
            canvas.drawRect(left, top, right, bottom, strokePaint)

            val full = 0.2 + 0.2 * i
            val empty = 0.2 * i
            top = if (currentValue >= full) {
                //draw full
                0f
            } else {
                if (currentValue <= empty) {
                    //draw empty
                    continue
                } else {
                    val fraction = (currentValue % 0.2 / 0.2f).toFloat()
                    val innerHeight = height - 2 * strokeWidth
                    strokeWidth + innerHeight - innerHeight * fraction
                }
            }
            canvas.drawRect(left, top, right, bottom, fillPaint)
        }
    }
}
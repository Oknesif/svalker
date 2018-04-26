package com.zzzombiecoder.svalker.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import java.util.*

class ArrowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var targetValue: Int = 0
    private var animatedValue: Float = 0f
    private val random = Random()
    private val valueAnimator = ValueAnimator()

    init {
        valueAnimator.duration = 2000
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.addUpdateListener {
            animatedValue = (it.animatedValue as Float)
            ViewCompat.postInvalidateOnAnimation(this)
        }

        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.strokeWidth = 5.toDP()
        paint.style = Paint.Style.STROKE
    }

    fun stop() {
        valueAnimator.cancel()
    }

    fun setTargetValue(value: Int) {
        targetValue = value
        valueAnimator.setFloatValues(animatedValue, (140f / 5) * targetValue + getRandomInterference())
        if (valueAnimator.isRunning.not()) {
            valueAnimator.start()
        }
    }

    private fun getRandomInterference(): Float {
        val rand = random.nextFloat()
        return rand * 6 - 3f
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        valueAnimator.cancel()
    }

    private fun Int.toDP(): Float {
        return this * resources.displayMetrics.density
    }

    override fun draw(canvas: Canvas) {
        val left = width / 2f
        val top = 15.toDP()
        val right = width / 2f
        val bottom = height.toFloat()


        canvas.save()
        canvas.rotate(animatedValue - 70f,
                width / 2f,
                height.toFloat() + 10.toDP())
        canvas.drawLine(left, top, right, bottom, paint)
        canvas.restore()

        super.draw(canvas)
    }
}
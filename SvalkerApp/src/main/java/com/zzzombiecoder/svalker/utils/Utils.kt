package com.zzzombiecoder.svalker.utils

import android.support.v4.view.GestureDetectorCompat
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    this.add(disposable)
}

fun View.setOnFlingListener(listener: (velocityX: Float, velocityY: Float) -> Unit) {
    val onGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?,
                             velocityX: Float, velocityY: Float): Boolean {
            listener.invoke(velocityX, velocityY)
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
    }

    val gestureDetector = GestureDetectorCompat(this.context, onGestureListener)
    this.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
}
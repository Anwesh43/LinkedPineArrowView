package com.anwesh.uiprojects.pinearrowview

/**
 * Created by anweshmishra on 28/01/19.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color

val nodes : Int = 5
val lines : Int = 4
val scGap : Float = 0.05f
val lastStep : Int = 2
val scDiv : Double = 0.51
val foreColor : Int = Color.parseColor("#4527A0")
val backColor : Int = Color.parseColor("#BDBDBD")
val sizeFactor : Float = 2.9f
val strokeFactor : Int = 90

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()
fun Float.mirrorValue(a : Int, b : Int) : Float = (1 - scaleFactor()) * a.inverse() + scaleFactor() * b.inverse()
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * scGap * dir

fun Canvas.drawPine(i : Int, size : Float, sc : Float, paint : Paint) {
    for (i in 0..1) {
        val sf : Float = 1f - 2 * i
        save()
        translate(0f, size * i)
        drawLine(0f, 0f, size * sf * sc * sf, size/3 * sf * sc, paint)
        restore()
    }
}
fun Canvas.drawPANode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / sizeFactor
    val xGap : Float = (2 * size) / lines
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    val sc21 : Float = sc2.divideScale(0, lastStep)
    val sc22 : Float = sc2.divideScale(1, lastStep)
    val sf : Float = 1f - 2 * (i % 2)
    paint.color = foreColor
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    save()
    translate(w/2 + (w/2 + paint.strokeWidth/2) * sf * sc22, gap * (i + 1))
    rotate(90f * sf * sc21)
    drawLine(0f, 0f, 0f, 2 * size * sc1, paint)
    for (j in 0..(lines - 1)) {
        drawPine(j, xGap, sc1.divideScale(j, lines), paint)
    }
    restore()
}

class PineArrowView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                if (Math.abs(scale - prevScale) > 1) {
                    scale = prevScale + dir
                    dir = 0f
                    prevScale = scale
                    cb(prevScale)
                }
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }
}
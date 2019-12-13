package com.mzm.sample.digit_recognizer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * A custom view that allows the user to draw the digits
 * Note we are using a black background for the canvas and white stroke
 * to better match the MNIST training data
 */
class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var canvas: Canvas
    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var bitmap: Bitmap

    private var currX = 0f
    private var currY = 0f

    init {
        init()
    }

    private fun init() {
        val dm = context.resources.displayMetrics
        bitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        path = Path()
        paint = Paint()

        paint.isAntiAlias = true       // smooth out edges of drawing
        paint.isDither = true
        paint.color = Color.WHITE    // set stroke color to black
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 48f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)  // set canvas background to black
        canvas.drawBitmap(bitmap!!, 0f, 0f, paint)
        canvas.drawPath(path!!, paint!!)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touch_start(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touch_move(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touch_up()
                invalidate()
            }
        }
        super.onTouchEvent(event)
        return true
    }

    private fun touch_start(x: Float, y: Float) {
        path.reset()
        path.moveTo(x, y)
        this.currX = x
        this.currY = y
    }

    private fun touch_move(x: Float, y: Float) {
        val dx = Math.abs(x - this.currX)
        val dy = Math.abs(y - this.currY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(this.currX, this.currY, (x + this.currX) / 2, (y + this.currY) / 2)
            this.currX = x
            this.currY = y
        }
    }

    private fun touch_up() {
        path.lineTo(x, y)
        canvas.drawPath(path, paint)
        path.reset()
    }

    fun clear() {
        path.reset()
        bitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        invalidate()
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLACK)
        draw(canvas)
        return bitmap
    }

    companion object {
        private val TOUCH_TOLERANCE = 4f
    }
}

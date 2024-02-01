package com.rogoz208.hometask2customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import kotlin.math.min
import kotlin.random.Random

class WheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val wheelItems: MutableList<WheelItem> = mutableListOf()
    private val rect: RectF = RectF(100f, 100f, 100f, 100f)
    private val paint: Paint = Paint()
    private var arcs = emptyList<Arc>()
    private var winner = -1
    var image: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    init {
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        fillWheelItems()
        computeArcs()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size: Int = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawArcs(canvas)
        drawWinner(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    fun rotateWheelToRandomTarget() {
        val randomTarget: Int = Random.nextInt(0, wheelItems.size - 1)
        rotateWheelToTarget(randomTarget)
    }

    fun rotateWheelToTarget(target: Int) {
        resetView()
        val wheelItemCenter: Float = arcs[target].start + (arcs[target].sweepAngle / 2)
        animate().setInterpolator(DecelerateInterpolator())
            .setDuration(3000L)
            .setStartDelay(250L)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .rotation((360f * 15f) - wheelItemCenter)
            .withEndAction {
                winner = target
                invalidate()
            }
    }

    fun resetView() {
        resetRotation()
        winner = -1
        invalidate()
    }

    private fun resetRotation() {
        animate().setStartDelay(0L).setDuration(0L).rotation(0f)
    }

    private fun fillWheelItems() {
        wheelItems.addAll(
            listOf(
                WheelItem(color = Color.RED, text = "Красный"),
                WheelItem(color = Color.parseColor("#e8851c"), isImage = true),
                WheelItem(color = Color.YELLOW, text = "Жёлтый"),
                WheelItem(color = Color.GREEN, isImage = true),
                WheelItem(color = Color.CYAN, text = "Голубой"),
                WheelItem(color = Color.BLUE, isImage = true),
                WheelItem(color = Color.parseColor("#A020F0"), text = "Фиолетовый")
            )
        )
    }

    private fun computeArcs() {
        val sweepAngle: Float = 360f / wheelItems.size
        arcs = wheelItems.mapIndexed { index, item ->
            val startAngle = index * sweepAngle
            Arc(start = startAngle, sweepAngle = sweepAngle, color = item.color)
        }
        invalidate()
    }

    private fun drawArcs(canvas: Canvas) {
        arcs.forEach { arc ->
            paint.color = arc.color
            canvas.drawArc(rect, -90 + arc.start, arc.sweepAngle, true, paint)
        }
    }

    private fun drawWinner(canvas: Canvas) {
        if (winner in 0..<wheelItems.size && wheelItems[winner].isImage) {
            image?.let {
                drawImage(canvas, it)
            }
        } else if (winner in 0..<wheelItems.size) {
            drawText(canvas, wheelItems[winner].text.toString())
        }
    }

    private fun drawImage(canvas: Canvas, bitmap: Bitmap) {
        val rect = RectF(350.0f, 400.0f, 670.0f, 640.0f)
        canvas.drawBitmap(
            bitmap,
            null,
            rect,
            Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
        )
    }

    private fun drawText(canvas: Canvas, text: String) {
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 100f
        }
        canvas.drawText(text, 350f, 550f, textPaint)
    }
}

private class Arc(val start: Float, val sweepAngle: Float, val color: Int)
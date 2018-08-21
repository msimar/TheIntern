package io.github.msimar.theintern

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator


class TheInternView : View {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
    context,
    attrs,
    defStyle
  )

  private val wavePaint: Paint = Paint(ANTI_ALIAS_FLAG)
  private val textRectPaint: Paint = Paint()

  private val group1TextPaint: Paint = Paint()
  private val group2TextPaint: Paint = Paint()
  private val group3TextPaint: Paint = Paint()

  private val group1Text = "THE"
  private val group2Text = "I"
  private val group3Text = "NTERN"

  private var group1TextWidth: Int = 0
  private var group1TextHeight: Int = 0

  private var group2TextWidth: Int = 0
  private var group2TextHeight: Int = 0

  private var group3TextWidth: Int = 0
  private var group3TextHeight: Int = 0

  private val group1TextBounds = Rect()
  private val group2TextBounds = Rect()
  private val group3TextBounds = Rect()

  private var animatedValue: Int = 0

  private val blackRect1 = Rect()
  private val blackRect2 = Rect()
  private val blackRect3 = Rect()
  private val blackRect4 = Rect()

  private var textOrigin = 0f
  private var group1TextBaseLine = 0f
  private var group2TextBaseLine = 0f
  private var group3TextBaseLine = 0f

  private lateinit var textRect: RectF

  private var letterSpacing = 0f

  init {

    wavePaint.color = Color.WHITE

    textRectPaint.style = Paint.Style.STROKE
    textRectPaint.strokeWidth = 8f
    textRectPaint.color = Color.WHITE

    group1TextPaint.color = Color.WHITE
    group1TextPaint.textSize = 40f
    group1TextPaint.getTextBounds(group1Text, 0, group1Text.length, group1TextBounds)

    group2TextPaint.color = Color.WHITE
    group2TextPaint.textSize = 200f
    group2TextPaint.getTextBounds(group2Text, 0, group2Text.length, group2TextBounds)

    group3TextPaint.color = Color.WHITE
    group3TextPaint.textSize = 256f
    group3TextPaint.getTextBounds(group3Text, 0, group3Text.length, group3TextBounds)


    //group3TextWidth = group3TextBounds.width()
    group3TextWidth =
            group3TextPaint.measureText(group3Text).toInt() // Use measureText to calculate width
    group3TextHeight = group3TextBounds.height() // Use height from getTextBounds()

    group2TextWidth =
            group2TextPaint.measureText(group2Text).toInt() // Use measureText to calculate width
    group2TextHeight = group2TextBounds.height() // Use height from getTextBounds()

    group1TextWidth =
            group1TextPaint.measureText(group1Text).toInt() // Use measureText to calculate width
    group1TextHeight = group1TextBounds.height() // Use height from getTextBounds()

    setBackgroundColor(Color.BLACK)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    textOrigin = width / 2f - group3TextWidth / 2f
    group3TextBaseLine = height / 2f + group3TextHeight / 2f
    group2TextBaseLine = height / 2f + group3TextHeight / 2f
    group1TextBaseLine = height / 2f - group3TextHeight / 2f + group1TextBounds.height()

    textRect = RectF(
      width / 2f - group3TextWidth / 2f,
      height / 2f - group3TextHeight,
      group3TextBounds.right.toFloat() /* width / 2f + group3TextWidth / 2f */,
      height / 2f + group3TextHeight
    )
  }

  override fun draw(canvas: Canvas?) {
    super.draw(canvas)

    canvas?.drawRect(blackRect1, wavePaint)
    canvas?.drawRect(blackRect2, wavePaint)
    canvas?.drawRect(blackRect3, wavePaint)
    canvas?.drawRect(blackRect4, wavePaint)

    // L, T, R, B
    //canvas?.drawRoundRect(textRect, 8f, 8f, textRectPaint)

    canvas?.drawText(
      group1Text,
      textOrigin - group1TextWidth, group1TextBaseLine,
      group1TextPaint
    )

    canvas?.drawText(
      group2Text,
      textOrigin - group1TextWidth / 2 - group2TextWidth / 2, group2TextBaseLine,
      group2TextPaint
    )

    canvas?.drawText(
      group3Text,
      textOrigin, group3TextBaseLine,
      group3TextPaint
    )
  }

  fun magic() {
    animate(1, 0, height)
    animate(2, width, textOrigin.toInt(), 400)
    animate(3, height, (height / 2f + group3TextHeight).toInt(), 400 * 2)
    animate(4, width, group3TextBounds.right, 400 * 3)
  }

  private fun animate(step: Int, start: Int, end: Int, startDelay: Long = 0) {
    val animator = ValueAnimator.ofInt(start, end)
    animator.duration = 400
    animator.interpolator = DecelerateInterpolator()
    animator.addUpdateListener { animation ->
      animatedValue = animation.animatedValue as Int

      if (step == 1) {
        blackRect1.left = 0
        blackRect1.top = 0
        blackRect1.right = textOrigin.toInt()
        blackRect1.bottom = animatedValue
      } else if (step == 2) {
        blackRect2.left = animatedValue
        blackRect2.top = 0
        blackRect2.right = width
        blackRect2.bottom = (height / 2f - group3TextHeight).toInt()
      } else if (step == 3) {
        blackRect3.left = textOrigin.toInt()
        blackRect3.top = animatedValue
        blackRect3.right = width
        blackRect3.bottom = height
      } else if (step == 4) {
        blackRect4.left = animatedValue
        blackRect4.top = (height / 2f - group3TextHeight).toInt()
        blackRect4.right = width
        blackRect4.bottom = (height / 2f + group3TextHeight).toInt()
      }

      invalidate()
    }
    animator.startDelay = startDelay
    animator.start()
  }
}

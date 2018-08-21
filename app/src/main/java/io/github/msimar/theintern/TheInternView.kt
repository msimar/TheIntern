package io.github.msimar.theintern

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
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
  private val textPaint: Paint = Paint()

  private val text = "The Intern"
  private var textWidth: Int = 0 // Our calculated text bounds
  private var textHeight: Int = 0 // Our calculated text bounds

  private val textBounds = Rect()

  private lateinit var animator: ValueAnimator
  private var animatedValue: Int = 0

  private val blackRect1 = Rect()
  private val blackRect2 = Rect()
  private val blackRect3 = Rect()
  private val blackRect4 = Rect()

  private var textOrigin = 0f
  private var textBaseLine = 0f

  init {

    wavePaint.color = Color.WHITE

    textRectPaint.color = Color.BLACK

    textPaint.color = Color.WHITE
    textPaint.textSize = 128f

    // Now lets calculate the size of the text
    textPaint.getTextBounds(text, 0, text.length, textBounds)

    //textWidth = textBounds.width()
    textWidth = textPaint.measureText(text).toInt() // Use measureText to calculate width
    textHeight = textBounds.height() // Use height from getTextBounds()

    setBackgroundColor(Color.BLACK)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    textOrigin = width / 2f - textWidth / 2f
    textBaseLine = height / 2f + textHeight / 2f
  }

  override fun draw(canvas: Canvas?) {
    super.draw(canvas)

    canvas?.drawRect(blackRect1, wavePaint)
    canvas?.drawRect(blackRect2, wavePaint)
    canvas?.drawRect(blackRect3, wavePaint)
    canvas?.drawRect(blackRect4, wavePaint)

    // L, T, R, B
    canvas?.drawRect(
      width / 2f - textWidth / 2f, height / 2f - textHeight,
      width / 2f + textWidth / 2f, height / 2f + textHeight,
      textRectPaint
    )

    canvas?.drawText(
      "The Intern",
      textOrigin, textBaseLine,
      textPaint
    )
  }

  fun magic() {
    animate(1, 0, height)
    animate(2, width, textOrigin.toInt(), 400)
    animate(3, height, (height / 2f + textHeight).toInt(), 400*2)
    animate(4, width, (width / 2f + textWidth / 2f).toInt(), 400*3)
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
        blackRect2.bottom = (height / 2f - textHeight).toInt()
      } else if (step == 3) {
        blackRect3.left = textOrigin.toInt()
        blackRect3.top = animatedValue
        blackRect3.right = width
        blackRect3.bottom = height
      } else if (step == 4) {
        blackRect4.left = animatedValue
        blackRect4.top = (height / 2f - textHeight).toInt()
        blackRect4.right = width
        blackRect4.bottom = (height / 2f + textHeight).toInt()
      }

      invalidate()
    }
    animator.startDelay = startDelay
    animator.start()
  }
}

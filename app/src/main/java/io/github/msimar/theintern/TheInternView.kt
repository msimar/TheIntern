package io.github.msimar.theintern

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

enum class AnimDirection {
  TOP_TO_BOTTOM,
  TOP_RIGHT_TO_LEFT,
  BOTTOM_TO_TOP,
  MIDDLE_RIGHT_TO_LEFT
}

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

  private var group1TextBaseLine = 0f
  private var group2TextBaseLine = 0f
  private var group3TextBaseLine = 0f

  private lateinit var textRect: Rect

  private var textLeft = 0
  private var textRight = 0
  private var textTop = 0
  private var textBottom = 0

  private var animDirection: AnimDirection = AnimDirection.TOP_TO_BOTTOM

  init {

    wavePaint.color = Color.WHITE

    textRectPaint.style = Paint.Style.STROKE
    textRectPaint.strokeWidth = 8f
    textRectPaint.color = Color.BLACK

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
    // find out base line for the group text
    group3TextBaseLine = height / 2f + group3TextHeight / 2f
    group2TextBaseLine = height / 2f + group3TextHeight / 2f
    group1TextBaseLine = height / 2f - group3TextHeight / 2f + group1TextBounds.height()

    // measure bounds of the group text
    textLeft = width / 2 - group3TextWidth / 2 - group1TextWidth
    textRight = width / 2 + group3TextWidth / 2

    textTop = height / 2 - group3TextHeight
    textBottom = height / 2 + group3TextHeight

    // create text rect as per measured bounds
    textRect = Rect(
      textLeft,
      textTop,
      textRight,
      textBottom
    )
  }

  override fun draw(canvas: Canvas?) {
    super.draw(canvas)
    when (animDirection) {
      AnimDirection.TOP_TO_BOTTOM -> canvas?.drawRect(blackRect1, wavePaint)
      AnimDirection.TOP_RIGHT_TO_LEFT -> {
        canvas?.drawRect(blackRect1, wavePaint)
        canvas?.drawRect(blackRect2, wavePaint)
      }
      AnimDirection.BOTTOM_TO_TOP -> {
        canvas?.drawRect(blackRect1, wavePaint)
        canvas?.drawRect(blackRect2, wavePaint)
        canvas?.drawRect(blackRect3, wavePaint)
      }
      AnimDirection.MIDDLE_RIGHT_TO_LEFT -> {
        canvas?.drawRect(blackRect1, wavePaint)
        canvas?.drawRect(blackRect2, wavePaint)
        canvas?.drawRect(blackRect3, wavePaint)
        canvas?.drawRect(blackRect4, wavePaint)
      }
    }

    //draw rect to check if text bounds are correct, as we expected
    //canvas?.drawRect(textRect, textRectPaint)

    // draw "The"
    canvas?.drawText(
      group1Text,
      width / 2f - group3TextWidth / 2f - group1TextWidth, group1TextBaseLine,
      group1TextPaint
    )

    // draw "I"
    canvas?.drawText(
      group2Text,
      width / 2f - group3TextWidth / 2f - group1TextWidth / 2 - group2TextWidth / 2,
      group2TextBaseLine,
      group2TextPaint
    )

    // draw "NTERN"
    canvas?.drawText(
      group3Text,
      width / 2f - group3TextWidth / 2f, group3TextBaseLine,
      group3TextPaint
    )
  }

  fun reset() {
    // Switch paint from White to Black for rect's
    wavePaint.color = Color.BLACK

    // reset rect to full size
    blackRect1.left = 0
    blackRect1.top = 0
    blackRect1.right = textLeft
    blackRect1.bottom = height

    blackRect2.left = width
    blackRect2.top = 0
    blackRect2.right = textLeft
    blackRect2.bottom = textTop

    blackRect3.left = textLeft
    blackRect3.top = textBottom
    blackRect3.right = width
    blackRect3.bottom = height

    blackRect4.left = textRight
    blackRect4.top = textTop
    blackRect4.right = width
    blackRect4.bottom = textBottom

    // re-draw as per changes
    invalidate()
  }

  fun execute() {
    // Switch paint from Black to White for rect's
    wavePaint.color = Color.WHITE
    // start animation from Top to Bottom
    animate(AnimDirection.TOP_TO_BOTTOM, 0, height)
  }

  private fun animate(direction: AnimDirection, start: Int, end: Int, startDelay: Long = 0) {
    // update the animation direction for canvas knowledge
    this.animDirection = direction

    val animator = ValueAnimator.ofInt(start, end)
    animator.duration = 400
    animator.interpolator = DecelerateInterpolator()
    animator.addUpdateListener { animation ->
      animatedValue = animation.animatedValue as Int

      when (direction) {
        AnimDirection.TOP_TO_BOTTOM -> {
          blackRect1.left = 0
          blackRect1.top = 0
          blackRect1.right = textLeft
          blackRect1.bottom = animatedValue
        }
        AnimDirection.TOP_RIGHT_TO_LEFT -> {
          blackRect2.left = animatedValue
          blackRect2.top = 0
          blackRect2.right = width
          blackRect2.bottom = textTop
        }
        AnimDirection.BOTTOM_TO_TOP -> {
          blackRect3.left = textLeft
          blackRect3.top = animatedValue
          blackRect3.right = width
          blackRect3.bottom = height
        }
        AnimDirection.MIDDLE_RIGHT_TO_LEFT -> {
          blackRect4.left = animatedValue
          blackRect4.top = textTop
          blackRect4.right = width
          blackRect4.bottom = textBottom
        }
      }

      invalidate()
    }
    animator.startDelay = startDelay
    animator.interpolator = FastOutSlowInInterpolator()
    animator.addListener(object : SimpleAnimatorListener {

      override fun onAnimationEnd(animation: Animator?) {
        // go to the next step as per preview direction
        when (direction) {
          AnimDirection.TOP_TO_BOTTOM -> animate(
            AnimDirection.TOP_RIGHT_TO_LEFT,
            width,
            textLeft
          )
          AnimDirection.TOP_RIGHT_TO_LEFT -> animate(
            AnimDirection.BOTTOM_TO_TOP,
            height,
            textBottom
          )
          AnimDirection.BOTTOM_TO_TOP -> animate(
            AnimDirection.MIDDLE_RIGHT_TO_LEFT,
            width,
            textRight
          )
          AnimDirection.MIDDLE_RIGHT_TO_LEFT -> {
            // do nothing, end of animation
          }
        }
      }
    })
    animator.start()
  }
}

/**
 * Extension to wrap AnimatorListener methods
 */
interface SimpleAnimatorListener : Animator.AnimatorListener {
  override fun onAnimationRepeat(animation: Animator?) {}

  override fun onAnimationEnd(animation: Animator?) {}

  override fun onAnimationCancel(animation: Animator?) {}

  override fun onAnimationStart(animation: Animator?) {}
}

package com.fansipan.callcolor.calltheme.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fansipan.callcolor.calltheme.R

@SuppressLint("AppCompatCustomView")
class TextViewOutline : TextView {
    companion object {
        const val DEFAULT_STROKE_WIDTH = 0
    }

    private var _strokeColor = 0
    private var _strokeWidth = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(attributeSet)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        attrs?.let {
            val obtain = context.obtainStyledAttributes(attrs, R.styleable.TextViewOutline)
            _strokeColor = obtain.getColor(
                R.styleable.TextViewOutline_outlineColor,
                ContextCompat.getColor(context, R.color.black)
            )
            _strokeWidth = obtain.getInt(R.styleable.TextViewOutline_outlineWidth, 0)
            obtain.recycle()
        } ?: kotlin.run {
            _strokeColor = currentTextColor
            _strokeWidth = DEFAULT_STROKE_WIDTH
        }
        _strokeWidth = dpToPx(context, _strokeWidth)
    }

    override fun onDraw(canvas: Canvas) {
        if (_strokeWidth > 0) {
            //set paint to fill mode
            val p: Paint = paint
            p.style = Paint.Style.FILL
            //draw the fill part of text
            super.onDraw(canvas)
            //save the text color
            val currentTextColor = currentTextColor
            //set paint to stroke mode and specify
            //stroke color and width
            p.style = Paint.Style.STROKE
            p.strokeWidth = _strokeWidth.toFloat()
            setTextColor(_strokeColor)
            //draw text stroke
            super.onDraw(canvas)
            //revert the color back to the one
            //initially specified
            setTextColor(currentTextColor)
        } else {
            super.onDraw(canvas)
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun setTextStrokeColor(color: String) {
        _strokeColor = Color.parseColor(color)
        invalidate()
    }
}
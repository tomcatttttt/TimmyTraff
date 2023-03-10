package com.nik.spinslot.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import com.nik.spinslot.R
import androidx.palette.graphics.Palette



class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var backroundDrawable: Drawable? = null

    fun setBackgroundImage(drawable: Drawable?) {
        backroundDrawable = drawable
        invalidate()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        backroundDrawable?.let {
            it.setBounds(0, 0, width, height)
            it.draw(canvas!!)
        }

        super.onDraw(canvas)
        val typeface1 = ResourcesCompat.getFont(context, R.font.fontstyle)

        val paint = Paint().apply {
                val bitmap = Color.YELLOW
            setColor(bitmap)
            textSize = 108f
            typeface = typeface1
            textAlign = Paint.Align.CENTER
        }

        val textX = width / 2f
        val textY = height / 2f + 40f
        canvas?.drawText("SPIN!", textX, textY, paint)
    }
}

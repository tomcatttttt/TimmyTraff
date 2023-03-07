package com.nik.spinslot.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.palette.graphics.Palette
import com.nik.spinslot.R


class CustomMaxButtton @JvmOverloads constructor(
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
            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.btnspin, null)
            val color = drawable?.let { drawable ->
                val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                Palette.from(bitmap).generate().getDominantColor(Color.BLACK)
            } ?: Color.BLACK
            setColor(color)
            textSize = 88f
            typeface = typeface1
            textAlign = Paint.Align.CENTER
        }

        val textX = width / 2f
        val textY = height / 2f + 35f
        canvas?.drawText("MAX!", textX, textY, paint)
    }
}
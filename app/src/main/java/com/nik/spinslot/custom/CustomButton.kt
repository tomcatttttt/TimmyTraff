package com.nik.spinslot.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

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
    }
}

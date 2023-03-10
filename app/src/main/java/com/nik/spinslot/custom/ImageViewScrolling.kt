package com.nik.spinslot.custom

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.nik.spinslot.IEventEnd
import com.nik.spinslot.R
import com.nik.spinslot.ui.main.MainViewModel
import kotlinx.android.synthetic.main.image_view_scrolling.view.*

class ImageViewScrolling @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var viewModel: MainViewModel
    var last_result = 0
    var oldValue = 0
    companion object {
        private const val ANIMATION_DURATION = 150
    }

    val value: Int
        get() = Integer.parseInt(nextImage.tag.toString())
    internal lateinit var eventEnd: IEventEnd

    fun setEventEnd(eventEnd: (result: Int, count: Int) -> Unit) {
        this.eventEnd = object : IEventEnd {
            override fun eventEnd(result: Int, count: Int) {
                eventEnd(result, count)
            }
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.image_view_scrolling, this)
        nextImage.translationY = nextImage.height.toFloat() // измененное значение
    }

    fun setValueRandom(image: Int, num_rotate: Int) {
        currentImage.animate()
            .translationY((-height).toFloat())
            .setDuration(ANIMATION_DURATION.toLong()).start()
        nextImage.translationY = nextImage.height.toFloat()

        nextImage.animate().translationY(0f).setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    setImage(currentImage, oldValue % 6)
                    currentImage.translationY = 0f
                    if (oldValue != num_rotate) { // if still have rotate
                        setValueRandom(image, num_rotate)
                        oldValue++
                    } else {
                        last_result = 0
                        oldValue = 0

                        setImage(nextImage, image)
                        eventEnd.eventEnd(image % 6, num_rotate)
                        //Because we have 6 images
                    }
                    nextImage.translationY = nextImage.height.toFloat()
                }

                override fun onAnimationCancel(animation: Animator) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationRepeat(animation: Animator) {
                    TODO("Not yet implemented")
                }
            }).start()
    }


    enum class Images(val drawableId: Int) {
        Vinograd(0),
        DIAMOND(1),
        CHERRY(2),
        APPLE(3),
        LEMON(4),
        WATERMELON(5)
    }
    fun setImage(img: ImageView?, value: Int) {
        if (value == Images.Vinograd.drawableId)
            img!!.setImageResource(R.drawable.vonograd)
        else if (value == Images.DIAMOND.drawableId)
            img!!.setImageResource(R.drawable.diamond)
        else if (value == Images.CHERRY.drawableId)
            img!!.setImageResource(R.drawable.cherry)
        else if (value == Images.APPLE.drawableId)
            img!!.setImageResource(R.drawable.apple)
        else if (value == Images.LEMON.drawableId)
            img!!.setImageResource(R.drawable.lemon)
        else if (value == Images.WATERMELON.drawableId)
            img!!.setImageResource(R.drawable.watermelon)

        img!!.tag = value
        last_result = value
    }
}
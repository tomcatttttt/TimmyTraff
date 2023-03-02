package com.nik.spinslot.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import com.nik.spinslot.Common
import com.nik.spinslot.R
import com.nik.spinslot.custom.IEventEnd
import com.nik.spinslot.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.random.Random

class MainFragment : Fragment(), IEventEnd {

    internal var count_down = 0

    private lateinit var viewModel: MainViewModel
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMainBinding.inflate(inflater, container, false).apply {
        image.setEventEnd(this@MainFragment)
        image2.setEventEnd(this@MainFragment)
        image3.setEventEnd(this@MainFragment)
        buttonSpin.setBackgroundImage(resources.getDrawable(R.drawable.btngreen))
        buttonMax.apply {
            setBackgroundImage(resources.getDrawable(R.drawable.btnpurple))
        }
        buttonMax.setOnClickListener{
            buttonMax.isEnabled = false
            viewModel.currentValue = Common.SCORE
            tvBet.text = viewModel.currentValue.toString()
        }
            buttonSpin.setOnClickListener {
                if (Common.SCORE>=10) {
                    if (Common.SCORE >= viewModel.currentValue) {
                        buttonSpin.isEnabled = false

                        image.setValueRandom(
                            Random.nextInt(6),
                            Random.nextInt(15 - 5 + 1) + 5
                        )
                        image2.setValueRandom(
                            Random.nextInt(6),
                            Random.nextInt(15 - 5 + 1) + 5
                        )
                        image3.setValueRandom(
                            Random.nextInt(6),
                            Random.nextInt(15 - 5 + 1) + 5
                        )

                        Common.SCORE -= viewModel.currentValue
                        tvScore.text = Common.SCORE.toString()
                    } else {
                        Toast.makeText(context, "Tou lose", Toast.LENGTH_SHORT).show()
                    }
                }
                else{Toast.makeText(context, "Sorry, you don't have money", Toast.LENGTH_SHORT).show()}
            }
        btnPlus.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // When button is pressed, start incrementing the counter by 10 every 100 milliseconds
                    handler.postDelayed(@SuppressLint("ClickableViewAccessibility")
                    object : Runnable {
                        override fun run() {
                            val currentValue = viewModel.currentValue
                            if (currentValue + 100 <= Common.SCORE) {
                                viewModel.currentValue = currentValue + 100
                                tvBet.text = viewModel.currentValue.toString()
                            } else {
                                viewModel.currentValue = Common.SCORE
                                tvBet.text = viewModel.currentValue.toString()
                                Toast.makeText(
                                    context,
                                    "Maximum bet is ${Common.SCORE}.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            handler.postDelayed(this, 100)
                        }
                    }, 1000
                    )
                    // Increment counter by 1 on button click
                    if (viewModel.currentValue + 10 <= Common.SCORE) {
                        viewModel.currentValue = viewModel.currentValue + 10
                        tvBet.text = viewModel.currentValue.toString()
                    } else {
                        Toast.makeText(
                            context,
                            "Maximum bet is ${Common.SCORE}.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // When button is released, stop incrementing the counter
                    handler.removeCallbacksAndMessages(null)
                }
            }
            true
        }

        btnMinus.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // When button is pressed, start decrementing the counter by 10 every 100 milliseconds
                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            var currentValue = viewModel.currentValue
                            currentValue -= 100
                            if (currentValue >= 10) {
                                viewModel.currentValue = currentValue
                                tvBet.text = currentValue.toString()
                            } else {
                                viewModel.currentValue = 10
                                tvBet.text = viewModel.currentValue.toString()
                                Toast.makeText(context, "Minimum bet is 10.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            handler.postDelayed(this, 100)
                        }
                    }, 1000)
                    // Decrement counter by 1 on button click
                    var currentValue = viewModel.currentValue
                    currentValue -= 10
                    if (currentValue >= 10) {
                        viewModel.currentValue = currentValue
                        tvBet.text = currentValue.toString()
                    } else {
                        Toast.makeText(context, "Minimum bet is 10.", Toast.LENGTH_SHORT).show()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // When button is released, stop decrementing the counter
                    handler.removeCallbacksAndMessages(null)
                }
            }
            true
        }

    }.root

    override fun eventEnd(result: Int, count: Int) {
        if (count_down < 2)
            count_down++ // If still have image scrolling
        else {
            buttonSpin.isEnabled = true
            buttonMax.isEnabled = true

            count_down = 0

            if (image.value == image2.value && image2.value == image3.value) {
                Toast.makeText(context, "You won BIG prize", Toast.LENGTH_SHORT).show()
                Common.SCORE += viewModel.currentValue * 2
                tvScore.text =  Common.SCORE.toString()
                tvBet.text = 10.toString()


            } else if (image.value == image2.value
                || image2.value == image3.value
                || image.value == image3.value
            ) {
                Toast.makeText(context, "You won SMALL prize", Toast.LENGTH_SHORT).show()
                Common.SCORE += viewModel.currentValue / 2
                tvScore.text =  Common.SCORE.toString()
                tvBet.text = 10.toString()
            } else {
                Toast.makeText(context, "You lost", Toast.LENGTH_SHORT).show()
                tvBet.text = 10.toString()

            }
        }
    }

}
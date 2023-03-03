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
import com.nik.spinslot.R
import com.nik.spinslot.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.random.Random

class MainFragment : Fragment() {

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
        var currentBalance = viewModel.balance
        image.setEventEnd { result: Int, count: Int ->
            eventEnd(result, count)
        }
        image2.setEventEnd { result: Int, count: Int ->
            eventEnd(result, count)
        }
        image3.setEventEnd { result: Int, count: Int ->
            eventEnd(result, count)
        }

        buttonSpin.setBackgroundImage(resources.getDrawable(R.drawable.btngreen))
        buttonMax.setBackgroundImage(resources.getDrawable(R.drawable.btnpurple))

        buttonMax.setOnClickListener {
            buttonMax.isEnabled = false
            viewModel.bet = currentBalance
            tvBet.text = viewModel.bet.toString()
        }
        buttonSpin.setOnClickListener {
            buttonSpin.isEnabled = false

            if (currentBalance >= 10 && currentBalance >= viewModel.bet) {
                if (currentBalance >= viewModel.bet) {
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

                    currentBalance -= viewModel.bet
                    tvScore.text = currentBalance.toString()
                } else {
                    Toast.makeText(context, "You lose", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Sorry, you don't have money", Toast.LENGTH_SHORT).show()
            }
        }



        btnPlus.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            viewModel.bet = viewModel.incrementCurrentValue(viewModel.bet, currentBalance)
                            tvBet.text = viewModel.bet.toString()
                        }
                    }, 1000)
                    viewModel.bet = viewModel.incrementCurrentValue(viewModel.bet, currentBalance)
                    tvBet.text = viewModel.bet.toString()
                }
                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacksAndMessages(null)
                }
            }
            true
        }

        btnMinus.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // when button is pressed, start decreasing counter by 10 every 100 milliseconds
                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            var currentValue = viewModel.bet
                            currentValue = viewModel.decrementCurrentValue(currentValue - 100)
                            viewModel.bet = currentValue
                            tvBet.text = currentValue.toString()
                            handler.postDelayed(this, 100)
                        }
                    }, 1000)
                    // decrement counter by 1 when the button is clicked
                    var currentValue = viewModel.bet
                    currentValue = viewModel.decrementCurrentValue(currentValue - 10)
                    viewModel.bet = currentValue
                    tvBet.text = currentValue.toString()
                }
                MotionEvent.ACTION_UP -> {
                    // When button is released, stop decrement counter
                    handler.removeCallbacksAndMessages(null)
                }
            }
            true
        }

    }.root

    val eventEnd: (result: Int, count: Int) -> Unit = { result, count ->
        if (count_down < 2)
            count_down++ // If still have image scrolling
        else {
            buttonSpin.isEnabled = true
            buttonMax.isEnabled = true

            count_down = 0

            if (image.value == image2.value && image2.value == image3.value) {
                Toast.makeText(context, "You won BIG prize", Toast.LENGTH_SHORT).show()
                viewModel.balance += viewModel.bet * 2
                tvScore.text = viewModel.balance.toString()
                tvBet.text = 10.toString()
            } else if (image.value == image2.value || image2.value == image3.value || image.value == image3.value) {
                Toast.makeText(context, "You won SMALL prize", Toast.LENGTH_SHORT).show()
                viewModel.balance += viewModel.bet / 2
                tvScore.text = viewModel.balance.toString()
                tvBet.text = 10.toString()
            } else {
                Toast.makeText(context, "You lost", Toast.LENGTH_SHORT).show()
                tvBet.text = 10.toString()
            }
        }
    }

}
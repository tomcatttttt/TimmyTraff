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
        tvBet.text = viewModel.bet.toString()
        tvScore.text = viewModel.balance.toString()
        image.setEventEnd { result: Int, count: Int ->
            eventEnd(result, count)
        }
        image2.setEventEnd { result: Int, count: Int ->
            eventEnd(result, count)
        }
        image3.setEventEnd { result: Int, count: Int ->
            eventEnd(result, count)
        }

        buttonSpin.setBackgroundImage(resources.getDrawable(R.drawable.btnspin))
        buttonMax.setBackgroundImage(resources.getDrawable(R.drawable.btnmax))

        buttonMax.setOnClickListener {
            buttonMax.isEnabled = false
            viewModel.bet = viewModel.balance
            tvBet.text = viewModel.bet.toString()
        }
        buttonSpin.setOnClickListener {
            buttonSpin.isEnabled = false

            if (viewModel.balance >= 10 && viewModel.balance >= viewModel.bet) {
                if (viewModel.balance >= viewModel.bet) {
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

                    viewModel.balance -= viewModel.bet
                    tvScore.text = viewModel.balance.toString()
                } else {
                    Toast.makeText(context, "You lose", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Sorry, you don't have money", Toast.LENGTH_SHORT).show()
            }
            if (viewModel.balance >= 100){
                viewModel.bet = 100
            } else{viewModel.bet = 0}
        }


        btnPlus.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // When button is pressed, start incrementing the counter by 10 every 100 milliseconds
                    handler.postDelayed(
                        @SuppressLint("ClickableViewAccessibility")
                        object : Runnable {
                            override fun run() {
                                val currentValue = viewModel.bet
                                if (currentValue + 100 <= viewModel.balance) {
                                    viewModel.bet = currentValue + 100
                                    tvBet.text = viewModel.bet.toString()
                                } else {
                                    viewModel.bet = viewModel.balance
                                    tvBet.text = viewModel.bet.toString()
                                    Toast.makeText(
                                        context,
                                        "Maximum bet is ${viewModel.balance}.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                handler.postDelayed(this, 100)
                            }
                        }, 1000
                    )
                    // Increment counter by 1 on button click
                    if (viewModel.bet + 10 <= viewModel.balance) {
                        viewModel.bet = viewModel.bet + 10
                        tvBet.text = viewModel.bet.toString()
                    } else {
                        Toast.makeText(
                            context,
                            "Maximum bet is ${viewModel.balance}.",
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
                            var currentValue = viewModel.bet
                            currentValue -= 100
                            if (currentValue >= 10) {
                                viewModel.bet = currentValue
                                tvBet.text = currentValue.toString()
                            } else {
                                viewModel.bet = 10
                                tvBet.text = viewModel.bet.toString()
                                Toast.makeText(context, "Minimum bet is 10.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            handler.postDelayed(this, 100)
                        }
                    }, 1000)
                    // Decrement counter by 1 on button click
                    var currentValue = viewModel.bet
                    currentValue -= 10
                    if (currentValue >= 10) {
                        viewModel.bet = currentValue
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
                tvBet.text = viewModel.bet.toString()
            } else if (image.value == image2.value || image2.value == image3.value || image.value == image3.value) {
                Toast.makeText(context, "You won SMALL prize", Toast.LENGTH_SHORT).show()
                viewModel.balance += viewModel.bet / 2
                tvScore.text = viewModel.balance.toString()
                tvBet.text = viewModel.bet.toString()
            } else {
                Toast.makeText(context, "You lost", Toast.LENGTH_SHORT).show()
                tvBet.text = viewModel.bet.toString()
                tvScore.text = viewModel.balance.toString()
            }
        }
    }

}
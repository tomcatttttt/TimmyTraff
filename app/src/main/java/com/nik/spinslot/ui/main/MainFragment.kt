package com.nik.spinslot.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nik.spinslot.Common
import com.nik.spinslot.R
import com.nik.spinslot.databinding.FragmentMainBinding
import com.nik.spinslot.imageViewScroll.IEventEnd
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.random.Random

class MainFragment : Fragment(), IEventEnd {

    internal var count_down = 0
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )= FragmentMainBinding.inflate(inflater, container, false).apply {



        image.setEventEnd(this@MainFragment)
        image2.setEventEnd(this@MainFragment)
        image3.setEventEnd(this@MainFragment)


       up.setOnClickListener{
            if (Common.SCORE >= 50){
                up.visibility = View.GONE
                down.visibility = View.VISIBLE
                image.setValueRandom(Random.nextInt(6),
               Random.nextInt(15-5+1)+5 )
                image2.setValueRandom(Random.nextInt(6),
                    Random.nextInt(15-5+1)+5 )
                image3.setValueRandom(Random.nextInt(6),
                    Random.nextInt(15-5+1)+5 )

                Common.SCORE -= 50
                tvScore.text = Common.SCORE.toString()
            }
            else{
                Toast.makeText(context,"You not enough money", Toast.LENGTH_SHORT).show()
            }
        }
    }.root

    override fun eventEnd(result: Int, count: Int) {
        if (count_down < 2)
            count_down++ // If still have image scrolling
        else {
            down.visibility = View.GONE
            up.visibility = View.VISIBLE

            count_down = 0

            if (image.value == image2.value && image2.value == image3.value) {
                Toast.makeText(context, "You won BIG prize", Toast.LENGTH_SHORT).show()
                Common.SCORE += 300
                tvScore.text = Common.SCORE.toString()
            } else if (image.value == image2.value
                || image2.value == image3.value
                || image.value == image3.value) {
                Toast.makeText(context, "You won SMALL prize", Toast.LENGTH_SHORT).show()
                Common.SCORE += 100
                tvScore.text = Common.SCORE.toString()
            } else {
                Toast.makeText(context, "You lost", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
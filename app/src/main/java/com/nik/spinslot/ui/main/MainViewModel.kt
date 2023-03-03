package com.nik.spinslot.ui.main

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var balance = 1000
    var bet: Int = 100

    fun incrementCurrentValue(currentValue: Int, balance: Int) : Int {
        return if (currentValue + 10 <= balance) {
            currentValue + 10
        } else {
            balance
        }
    }

    fun decrementCurrentValue(currentValue: Int) : Int {
        return if (currentValue - 10 >= 10) {
            currentValue - 10
        } else {
            10
        }
    }
}


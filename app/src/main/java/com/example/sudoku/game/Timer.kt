package com.example.sudoku.game

import android.os.SystemClock
import android.widget.Chronometer

class Timer {

    companion object {
        var timer: Chronometer? = null

        fun beginTimer() {
            timer!!.setBase(SystemClock.elapsedRealtime())
            timer?.start()
        }

        fun stopTimer() {
            timer!!.stop()

        }
    }

}
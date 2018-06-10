package com.shawnzhong.game2048

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView

/**
 * Created by Shawn on 2017/6/5.
 * Card
 */

class Card constructor(context: Context, private val size: Int = 4) : FrameLayout(context) {
    var num = 0
        set(num) {
            field = num
            label.text = if (num <= 0) "" else "$num"
            label.textSize = (if (num >= 128) 160 / size else 192 / size).toFloat()
            label.setTextColor(if (num >= 8) 0xfff9f6f2.toInt() else 0xff776e65.toInt())


            val shape = GradientDrawable()
            shape.cornerRadius = (50 / size).toFloat()

            when (num) {
                0 -> shape.setColor(0xffcdc1b4.toInt())
                2 -> shape.setColor(0xffeee4da.toInt())
                4 -> shape.setColor(0xffede0c8.toInt())
                8 -> shape.setColor(0xfff2b179.toInt())
                16 -> shape.setColor(0xfff59563.toInt())
                32 -> shape.setColor(0xfff67c5f.toInt())
                64 -> shape.setColor(0xfff65e3b.toInt())
                128 -> shape.setColor(0xffedcf72.toInt())
                256 -> shape.setColor(0xffedcc61.toInt())
                512 -> shape.setColor(0xffedc850.toInt())
                1024 -> shape.setColor(0xffedc53f.toInt())
                2048 -> shape.setColor(0xffedc22e.toInt())
            }
            label.background = shape
        }
    private val label = TextView(getContext())

    init {
        label.gravity = Gravity.CENTER

        val lp = FrameLayout.LayoutParams(-1, -1)
        lp.setMargins(10, 10, 10, 10)
        addView(label, lp)
    }
}

package com.shawnzhong.game2048

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.GridLayout

class GameView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : GridLayout(context, attrs, defStyleAttr) {
    var size = 4
        set(size) {
            field = size
            removeAllViews()
            columnCount = size
            cardsMap = Array(size) { Array(size) { Card(context, size) } }

            cardsMap.deepForEach { addView(it, width / size, width / size) }
            startGame()
        }
    private lateinit var cardsMap: Array<Array<Card>>
    private fun Array<Array<Card>>.deepForEach(action: (Card) -> Unit) = this.forEach { it.forEach { action(it) } }

    init {
        setBackgroundColor(0xffbbada0.toInt())

        setOnTouchListener(object : View.OnTouchListener {
            private var startX = 0f
            private var startY = 0f
            private var offsetX = 0f
            private var offsetY = 0f

            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = motionEvent.x
                        startY = motionEvent.y
                    }
                    MotionEvent.ACTION_UP -> {
                        view.performClick()

                        offsetX = Math.abs(motionEvent.x - startX)
                        offsetY = Math.abs(motionEvent.y - startY)

                        when {
                            Math.abs(offsetX) > Math.abs(offsetY) && offsetX < -5 -> swipeLeft()
                            Math.abs(offsetX) > Math.abs(offsetY) && offsetX > 5 -> swipeRight()
                            Math.abs(offsetX) < Math.abs(offsetY) && offsetY < -5 -> swipeUp()
                            Math.abs(offsetX) < Math.abs(offsetY) && offsetY > 5 -> swipeDown()
                        }
                    }
                }
                return true
            }
        })

        post { size = size }
    }

    fun startGame() {
        MainActivity.mainActivity!!.score = 0
        cardsMap.deepForEach { it.num = 0 }
        repeat(2) { addRandomCard() }
    }

    private fun addRandomCard() {
        val emptyCards = mutableListOf<Card>()
        cardsMap.forEach { emptyCards += it.filter { it.num <= 0 } }
        emptyCards[(Math.random() * emptyCards.size).toInt()].num = if (Math.random() > 0.1) 2 else 4
        checkEnd()
    }

    private fun checkEnd() {
        for (x in 0 until size) {
            for (y in 0 until size) {
                val num = cardsMap[x][y].num
                if (num == 1024) {
                    AlertDialog.Builder(context).setTitle("WOW!").setMessage("You Win!").setPositiveButton("Restart") { _, _ -> startGame() }.show()
                    return
                }
                if (num <= 0 ||
                        x > 0 && num == cardsMap[x - 1][y].num ||
                        x < size - 1 && num == cardsMap[x + 1][y].num ||
                        y > 0 && num == cardsMap[x][y - 1].num ||
                        y < size - 1 && num == cardsMap[x][y + 1].num) {
                    return
                }
            }
        }
        AlertDialog.Builder(context).setTitle("Uh-oh...").setMessage("You Die!").setPositiveButton("Restart") { _, _ -> startGame() }.show()
    }

    private fun swipeLeft() {
        var move = false
        for (x in 0 until size) {
            var y = 0
            while (y < size) {
                for (y1 in y + 1 until size) {
                    if (cardsMap[x][y1].num > 0) {

                        val check = checkSwitch(cardsMap[x][y], cardsMap[x][y1])
                        move = move || check == 1 || check == 2
                        y -= if (check == 1) 1 else 0

                        break
                    }
                }
                y++

            }
        }
        if (move) addRandomCard()
    }


    private fun swipeRight() {
        var move = false
        for (x in 0 until size) {
            var y = size - 1
            while (y >= 0) {

                for (y1 in y - 1 downTo 0) {
                    if (cardsMap[x][y1].num > 0) {

                        val check = checkSwitch(cardsMap[x][y], cardsMap[x][y1])
                        move = move || check == 1 || check == 2
                        y += if (check == 1) 1 else 0

                        break
                    }
                }
                y--

            }
        }
        if (move) addRandomCard()
    }


    private fun swipeUp() {
        var move = false
        for (y in 0 until size) {
            var x = 0
            while (x < size) {

                for (x1 in x + 1 until size) {
                    if (cardsMap[x1][y].num > 0) {

                        val check = checkSwitch(cardsMap[x][y], cardsMap[x1][y])
                        move = move || check == 1 || check == 2
                        x -= if (check == 1) 1 else 0

                        break
                    }
                }
                x++

            }
        }

        if (move) addRandomCard()
    }

    private fun swipeDown() {
        var move = false
        for (y in 0 until size) {
            var x = size - 1
            while (x >= 0) {

                for (x1 in x - 1 downTo 0) {
                    if (cardsMap[x1][y].num > 0) {

                        val check = checkSwitch(cardsMap[x][y], cardsMap[x1][y])
                        move = move || check == 1 || check == 2
                        x += if (check == 1) 1 else 0
                        break
                    }
                }
                x--

            }
        }
        if (move) addRandomCard()
    }


    private fun checkSwitch(card1: Card, card2: Card): Int {
        val num1 = card1.num
        val num2 = card2.num

        if (num1 <= 0) {
            card1.num = num2
            card2.num = 0

            return 1
        } else if (num1 == num2) {
            card1.num = num1 * 2
            card2.num = 0
            MainActivity.mainActivity!!.score += num1 * 2

            return 2
        }
        return 0
    }
}



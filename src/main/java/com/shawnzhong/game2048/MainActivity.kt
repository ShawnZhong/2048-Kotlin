package com.shawnzhong.game2048

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    var score = 0
        set(score) {
            field = score
            (findViewById<TextView>(R.id.tvScore)).text = "$score"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivity = this
        gameView = findViewById<GameView>(R.id.gameView)
    }

    fun restart(view: View) = gameView.startGame()

    fun setting(view: View) {
        val dSetting = Dialog(this@MainActivity)
        dSetting.setContentView(R.layout.setting)

        val np = dSetting.findViewById<NumberPicker>(R.id.numberPicker1)
        np.maxValue = 10
        np.minValue = 3
        np.value = gameView.size
        np.wrapSelectorWheel = false


        (dSetting.findViewById<Button>(R.id.button1)).setOnClickListener {
            gameView.size = np.value
            dSetting.dismiss()
        }

        dSetting.show()
    }

    companion object {
        var mainActivity: MainActivity? = null
            private set
    }
}

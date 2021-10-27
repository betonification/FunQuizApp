package com.betonification.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_quiz_questions.*
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val username = intent.getStringExtra(Constants.USERNAME)
        val score = intent.getIntExtra(Constants.SCORE, 0)

        if (score > 5){
            tv_congratulations.text = "Well played $username"
        }else{
            tv_congratulations.text = "You suck $username"
        }

        pb_result.progress = score

        tv_result_points.text = (score*10).toString()

        tv_score.text = "Your score was ${score*10} points"


        btn_another_shot.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
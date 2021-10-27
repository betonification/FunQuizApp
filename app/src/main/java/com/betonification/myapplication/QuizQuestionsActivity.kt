package com.betonification.myapplication

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import kotlinx.android.synthetic.main.activity_quiz_questions.*
import kotlin.time.seconds

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {


    private var currentPosition = 1
    private var questionsList: ArrayList<Question>? = null
    private var selectedOptionPosition = 0
    private var correctAnswersNumber = 10
    private var selectedAnswer = false
    private var submittedAnswer = false
    private var userName: String? = null
    private var answeredCorrectly = true
    private var score = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        userName = intent.getStringExtra(Constants.USERNAME) // prihvata se podatak o imenu u userName promenjljivu kako bi se kasnije prosledio dalje do result activitija

        // preuzimanje liste pitanja i mesanje kako ne bi uvek isla istim redosledom
        questionsList = Constants.getQuestions()
        questionsList!!.shuffle()

        setQuestion()

        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit_or_next.setOnClickListener(this)

    }

    // funkcija za postavljanje pitanja, odnosno postavljanje pitanja i ponudjenih odgovora u njima namenjene view-ove
    fun setQuestion() {
        answeredCorrectly = true
        defaultOptionsView()
        selectedAnswer = false
        submittedAnswer = false
        if (currentPosition - 1 != questionsList?.size){
            btn_submit_or_next.text = "SUBMIT"
        }
        val question: Question = questionsList!![currentPosition - 1]
        pb_quiz_progress.progress = currentPosition
        tv_quiz_prgress.text = "$currentPosition" + "/" + pb_quiz_progress.max
        tv_question.text = question.question
        iv_flag.setImageResource(question.image)
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    // funkcija za postavljanje izgleda textview-ova ponudjenih odgovora u default stanje
    fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1,tv_option_two)
        options.add(2,tv_option_three)
        options.add(3,tv_option_four)
        for(option in options){
            option.setTextColor(Color.parseColor("#7A0809"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }


    override fun onClick(v: View?) {

        when(v?.id){
            R.id.tv_option_one -> {
                selectedOptionView(v as TextView, 1)
            }
            R.id.tv_option_two -> {
                selectedOptionView(v as TextView, 2)
            }
            R.id.tv_option_three -> {
                selectedOptionView(v as TextView, 3)
            }
            R.id.tv_option_four -> {
                selectedOptionView(v as TextView, 4)
            }
            R.id.btn_submit_or_next ->{
                if (selectedOptionPosition == 0 && submittedAnswer){
                    currentPosition++
                    when {
                        currentPosition <= questionsList!!.size -> {
                            setQuestion()
                        }else -> {
                            val intent = Intent(this,ResultActivity::class.java)
                            intent.putExtra(Constants.USERNAME, userName)
                            intent.putExtra(Constants.SCORE, correctAnswersNumber)
                            startActivity(intent)
                            finish()
                        }
                    }
                }else if (selectedAnswer){
                    val question = questionsList?.get(currentPosition - 1)
                    if (question!!.correctAnswer != selectedOptionPosition){
                        answerView(selectedOptionPosition, R.drawable.uncorrect_option_border_bg)
                        correctAnswersNumber--
                        answeredCorrectly = false
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                    if (answeredCorrectly){
                        score += 10
                    }
                    tv_points.text = "$score/100"
                    submittedAnswer = true
                    if(currentPosition == questionsList!!.size){
                        btn_submit_or_next.text = "FINISH"
                    }else{
                        btn_submit_or_next.text = "GO TO NEXT QUESTION"
                    }
                    selectedOptionPosition = 0
                }
            }
        }
    }

    // funkcija koja sluzi samo da olaksa postavljanje pozadine tacnog i netacnog odgovora
    private fun answerView(answer: Int, drawableView: Int){
        when (answer){
            1 -> tv_option_one.background = ContextCompat.getDrawable(this,drawableView)
            2 -> tv_option_two.background = ContextCompat.getDrawable(this,drawableView)
            3 -> tv_option_three.background = ContextCompat.getDrawable(this,drawableView)
            4 -> tv_option_four.background = ContextCompat.getDrawable(this,drawableView)
        }
    }

    //funkcija za postavljanje izgleda textview-a izabranog odgovora
    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int){
        // radi samo ukoliko nije vec dat konacan odgovor
        if(!submittedAnswer) {
            defaultOptionsView()
            selectedOptionPosition = selectedOptionNumber
            tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
            tv.setTypeface(tv.typeface, Typeface.BOLD)
            tv.setTextColor(Color.parseColor("#363A43"))
            selectedAnswer = true
        }
    }
}
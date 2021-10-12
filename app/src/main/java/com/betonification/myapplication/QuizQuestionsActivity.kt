package com.betonification.myapplication

import android.animation.ObjectAnimator
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)


        questionsList = Constants.getQuestions()
        questionsList!!.shuffle()
        setQuestion()

        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit_or_next.setOnClickListener(this)
    }
    fun setQuestion() {
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
                        Toast.makeText(this, "Congratulations", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else if (selectedAnswer){
                    val question = questionsList?.get(currentPosition - 1)
                    if (question!!.correctAnswer != selectedOptionPosition){
                        answerView(selectedOptionPosition, R.drawable.uncorrect_option_border_bg)
                        correctAnswersNumber--
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
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

    private fun answerView(answer: Int, drawableView: Int){
        when (answer){
            1 -> tv_option_one.background = ContextCompat.getDrawable(this,drawableView)
            2 -> tv_option_two.background = ContextCompat.getDrawable(this,drawableView)
            3 -> tv_option_three.background = ContextCompat.getDrawable(this,drawableView)
            4 -> tv_option_four.background = ContextCompat.getDrawable(this,drawableView)
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int){
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
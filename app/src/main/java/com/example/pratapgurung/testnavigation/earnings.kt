package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.TextView
/*
    Acitivity: earnings, this activity allow service provider to check his earnings
 */
class earnings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)
    }

    /*
        this method is called when user pressed daily earnings view and
         will start new activity called earning_details
     */
    fun getDailyEarnings(view: View){
        var btnClick = findViewById<TextView>(R.id.dailyEarnings).text.toString()
        startEarningdetails(btnClick);
    }

    /*
        this method is called when user pressed weekly earnings view and
         will start new activity called earning_details
     */
    fun getWeeklyEarnings(view: View){
        var btnClick = findViewById<TextView>(R.id.weeklyEarnings).text.toString()
        startEarningdetails(btnClick);
    }

    /*
        this method is called when user pressed monhtly earnings view and
         will start new activity called earning_details
     */
    fun getMonthlyEarnings(view: View){
        var btnClick = findViewById<TextView>(R.id.monthlyEarnings).text.toString()
        startEarningdetails(btnClick);
    }

    /*
        this method is called when user pressed yearly earnings view and
         will start new activity called earning_details
     */
    fun getYearlyEarnings(view: View){
        var btnClick = findViewById<TextView>(R.id.yearlyEarnings).text.toString()
        startEarningdetails(btnClick);
    }

    /*
        this method is called when user pressed total earnings view and
         will start new activity called earning_details
     */
    fun getTotalEarnings(view: View){
        var btnClick = findViewById<TextView>(R.id.totalEarnings).text.toString()
        startEarningdetails(btnClick);
    }

    /*
        this fun helper function and receives string represent of pressed btn
         will start new activity called earning_details
     */
    fun startEarningdetails(btnClick: String){

        val intent = Intent(this, Earning_Details::class.java)
        intent.putExtra("earning_contraint", btnClick )
        startActivity(intent)
    }

}

package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.TextView

class earnings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earnings)
    }

    fun getDailyEarnings(view: View){
        var btnClick = findViewById<TextView>(R.id.dailyEarnings).text.toString()
        startEarningdetails(btnClick);
    }
    fun getWeeklyEarnings(view: View){
        var btnClick = findViewById<TextView>(R.id.weeklyEarnings).text.toString()
        startEarningdetails(btnClick);
    }
    fun getMonthlyEarnings(view: View){
        var btnClick = findViewById<TextView>(R.id.monthlyEarnings).text.toString()
        startEarningdetails(btnClick);
    }
    fun getYearlyEarnings(view: View){
        var btnClick = findViewById<TextView>(R.id.yearlyEarnings).text.toString()
        startEarningdetails(btnClick);
    }
    fun getTotalEarnings(view: View){
        var btnClick = findViewById<TextView>(R.id.totalEarnings).text.toString()
        startEarningdetails(btnClick);
    }

    fun startEarningdetails(btnClick: String){

        val intent = Intent(this, Earning_Details::class.java)
        intent.putExtra("earning_contraint", btnClick )
        startActivity(intent)
    }

}

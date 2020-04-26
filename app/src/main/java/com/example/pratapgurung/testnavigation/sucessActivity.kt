package com.example.pratapgurung.testnavigation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class sucessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sucess)
        var intent = getIntent()
        var msg = intent.extras["message"].toString()
        var messageView = findViewById<TextView>(R.id.successMsg)
        messageView.setText(msg)

    }
}

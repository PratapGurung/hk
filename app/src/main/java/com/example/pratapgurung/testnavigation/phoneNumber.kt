package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//MainActivity_Agent
class phoneNumber : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    var  myRef = database.getReference().child("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()

        myRef.child(userId).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.
                val pNum = p0.child("phone").value.toString()
                val phNumberEditText  = findViewById<TextView>(R.id.phoneNumber)
                phNumberEditText.text = pNum

            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        })
    }

    fun retPhoneNumber(view : View){
        val text = findViewById<EditText>(R.id.phoneNumber).text.toString()
        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()

        //initialize db
        myRef.child(userId).child("phoneNumber").setValue(text)
        val myIntent = Intent(this@phoneNumber, profile::class.java)
        startActivity(myIntent);
    }
}

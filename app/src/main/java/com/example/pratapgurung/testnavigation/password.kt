package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
/*
    this activity allows to change their password and save it to db also
 */
class password : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    var  userRef = database.getReference().child("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

    }


    //save new password to db
    fun retClientPassword(view : View){
        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()
        val text = findViewById<EditText>(R.id.password).text.toString()
        //initialize db
        userRef.child(userId).child("password").setValue(text)
        val myIntent = Intent(this, profileCustomer::class.java)
        startActivity(myIntent);
    }
}

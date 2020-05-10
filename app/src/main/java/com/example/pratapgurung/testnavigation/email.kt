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

class email : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    private var  myRef = database.getReference().child("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()

        myRef.child(userId).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.
                val em = p0.child("email").value.toString()
                val emailEdit  = findViewById<TextView>(R.id.emailAdd)
                emailEdit.text = em
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        })
    }

    fun retEmail(view : View){
        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()
        val text = findViewById<EditText>(R.id.emailAdd).text.toString()
        //initialize db
        myRef.child(userId).child("email").setValue(text)
        val myIntent = Intent(this, profile::class.java)
        startActivity(myIntent);
    }
}

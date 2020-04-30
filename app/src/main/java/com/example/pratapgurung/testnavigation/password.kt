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

class password : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    var  myRef = database.getReference().child("user").child("1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.
                val pword = p0.child("Password").value.toString()
                val pwordEditText  = findViewById<TextView>(R.id.password)
                pwordEditText.text = pword

            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        })
    }

    fun retClientPassword(view : View){
        val text = findViewById<EditText>(R.id.password).text.toString()
        //initialize db
        myRef.child("Password").setValue(text)
        val myIntent = Intent(this, profile::class.java)
        startActivity(myIntent);
    }
}

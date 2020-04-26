package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class firstName : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    var  myRef = database.getReference().child("agents").child("1")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_name)
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.
                val fName = p0.child("firstName").value.toString()
                val firstName  = findViewById<TextView>(R.id.fNameInput)
                firstName.text = fName

            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        })
    }

    fun retClientFirstName(view : View){
        val text = findViewById<EditText>(R.id.fNameInput).text.toString()
        //initialize db
        myRef.child("firstName").setValue(text)
        val myIntent = Intent(this, profile::class.java)
        startActivity(myIntent);
    }
}

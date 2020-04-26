package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Write a message to the database
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference().child("agents").child("1")


        myRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.

                val fName = p0.child("firstName").value.toString()
                val lName = p0.child("lastName").value.toString()
                val pw = p0.child("Password").value.toString()
                val phNumber = p0.child("phoneNumber").value.toString()
                val em = p0.child("email").value.toString()


                val firstName  = findViewById<TextView>(R.id.clientFirstNameView)
                firstName.text = fName
                val lastName  = findViewById<TextView>(R.id.clientLastNameView)
                lastName.text = lName
                val password  = findViewById<TextView>(R.id.clientPassWordView)
                password.text = pw
                val email  = findViewById<TextView>(R.id.clientEmailView)
                email.text = em
                val phoneNumber  = findViewById<TextView>(R.id.clientPhoneNumView)
                phoneNumber.text = phNumber



            }


            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        })
    }
    fun processData(view: View){

    }
    //
    fun setFirstName(view : View){
        val myIntent = Intent(this, firstName::class.java)
        startActivity(myIntent)
        //findViewById<TextView>(R.id.clientFirstNameView).text = "Ryan"
    }

    //
    fun setSecondName(view : View){
        val myIntent = Intent(this, secondName::class.java)
        startActivity(myIntent)
        //findViewById<TextView>(R.id.clientFirstNameView).text = "Ryan"
    }
    fun setPassword(view : View){
        val myIntent = Intent(this, password::class.java)
        startActivity(myIntent)
        //findViewById<TextView>(R.id.clientFirstNameView).text = "Ryan"
    }

    fun setPhoneNumber(view : View){
        val myIntent = Intent(this, phoneNumber::class.java)
        startActivity(myIntent)
        //findViewById<TextView>(R.id.clientFirstNameView).text = "Ryan"
    }

    fun setEmail(view : View){
        val myIntent = Intent(this, email::class.java)
        startActivity(myIntent)
        //findViewById<TextView>(R.id.clientFirstNameView).text = "Ryan"
    }
}

package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.content.SharedPreferences
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
    var settings:SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Write a message to the database
        val database = FirebaseDatabase.getInstance()

        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("Username", "").toString()
        val myRef = database.getReference().child("user").child(userId)

        //get all the information from db
        myRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.

                val fName = p0.child("firstName").value.toString()
                val lName = p0.child("lastName").value.toString()
                var pw = p0.child("password").value.toString()
                val re = Regex(".")
                pw = re.replace(pw, "*")
                val phNumber = p0.child("phone").value.toString()
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

    fun setFirstName(view : View){
        val myIntent = Intent(this, firstName::class.java)
        startActivity(myIntent)
    }

    //
    fun setSecondName(view : View){
        val myIntent = Intent(this, secondName::class.java)
        startActivity(myIntent)

    }
    fun setPassword(view : View){
        val myIntent = Intent(this, password::class.java)
        startActivity(myIntent)

    }

    fun setPhoneNumber(view : View){
        val myIntent = Intent(this, phoneNumber::class.java)
        startActivity(myIntent)

    }

    fun setEmail(view : View){
        val myIntent = Intent(this, email::class.java)
        startActivity(myIntent)

    }
}

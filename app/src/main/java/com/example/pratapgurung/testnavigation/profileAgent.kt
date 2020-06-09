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

/*
    Activity Description: this activity create profile for logged in agent
                           and gets user information from database
 */

class profileAgent : AppCompatActivity() {
    //shared preference variable
    var settings:SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_agent)

        // initializae database instance
        val database = FirebaseDatabase.getInstance()

        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()

        //create reference to current logged in user in database
        val myRef = database.getReference().child("user").child(userId)

        //get all the required information from db
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

                //display the user information on view
                findViewById<TextView>(R.id.username).text = userId
                findViewById<TextView>(R.id.clientFirstNameView).text = fName
                findViewById<TextView>(R.id.clientLastNameView).text = lName
                findViewById<TextView>(R.id.clientPassWordView).text = pw
                findViewById<TextView>(R.id.clientEmailView).text = em
                findViewById<TextView>(R.id.clientPhoneNumView).text = phNumber

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        })
    }

    /*
        this method will be called when user pressed firstName view
        and start new activity called firstName
     */
    fun setFirstName(view : View){
        val myIntent = Intent(this, firstName::class.java)
        startActivity(myIntent)
    }

    /*
        this method will be called when user pressed LastName view
        and start new activity called secondName
     */
    fun setSecondName(view : View){
        val myIntent = Intent(this, secondName::class.java)
        startActivity(myIntent)

    }

    /*
        this method will be called when user pressed password view
        and start new activity called password
     */
    fun setPassword(view : View){
        val myIntent = Intent(this, password::class.java)
        startActivity(myIntent)

    }

    /*
        this method will be called when user pressed phonenumber view
        and start new activity called phonenumber
     */
    fun setPhoneNumber(view : View){
        val myIntent = Intent(this, phoneNumber::class.java)
        startActivity(myIntent)

    }
    /*
            this method will be called when user pressed email view
            and start new activity called email
     */
    fun setEmail(view : View){
        val myIntent = Intent(this, email::class.java)
        startActivity(myIntent)

    }
}

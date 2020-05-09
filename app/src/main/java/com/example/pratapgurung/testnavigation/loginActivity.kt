package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class loginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        val userName = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference().child("user").child(userName)

        // Read from the database
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val userType = dataSnapshot.child("userType").value.toString()
                if(dataSnapshot.exists()) {
                    //userexist
                    val dbpassword = dataSnapshot.child("password").value.toString()
                    if(password.equals(dbpassword)){
                        //save user data in sharedPreferences to save user information
                        val settings = getSharedPreferences("UserInfo", 0)
                        val editor = settings.edit()

                        val ratings = dataSnapshot.child("ratings").value.toString()
                        val firstName = dataSnapshot.child("firstName").value.toString()
                        val lastName = dataSnapshot.child("lastName").value.toString()
                        editor.putString("Username", firstName + " " + lastName) //save user name
                        editor.putString("ratings", ratings)
                        editor.putString("userId", userName)
                        editor.commit()//commit to save

                        loginStatus(true, userType)
                    }
                    else{
                        loginStatus(false,userType)
                    }

                }
                else{
                    loginStatus(false, userType)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

    }

    fun signUp(view: View) {

        val myIntent = Intent(this, SignUp::class.java)
        //myIntent.putExtra("message", "Service Request Sucessfully Submitted!!!")
        startActivity(myIntent);
    }

    fun loginStatus(b: Boolean, userType:String) {
        if(b){
            if(userType.equals("Customer")){
                val myIntent = Intent(this@loginActivity, MainActivity_Customer::class.java)
                startActivity(myIntent);
            }
            else{
                val myIntent = Intent(this@loginActivity, MainActivity_Agent::class.java)
                startActivity(myIntent);
            }

            displayToast("Login sucessfully!!")
        }
        else{
            displayToast("username and password does not match!!")
        }
    }

    fun displayToast(msg: String) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        // val toast = Toast.makeText(context, message, duration)
        val view = toast.view
        //Gets the actual oval background of the Toast then sets the colour filter
        view.background.setColorFilter(Color.parseColor("#19bd60"), PorterDuff.Mode.SRC_IN)
        //Gets the TextView from the Toast so it can be editted
        val text = view.findViewById<TextView>(android.R.id.message)
        text.setTextColor(Color.parseColor("#f7f7f7"))
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F);
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}

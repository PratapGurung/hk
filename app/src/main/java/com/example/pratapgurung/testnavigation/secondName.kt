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
    this activity allows to change their second name and save it to db also
 */
class secondName : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    var  myRef = database.getReference().child("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_name)

        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()

        myRef.child(userId).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.
                val lName = p0.child("lastName").value.toString()
                val LastName  = findViewById<TextView>(R.id.lastName)
                LastName.text = lName

            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        })
    }

    //save new last name to db
    fun retClientSecondName(view : View){
        val text = findViewById<EditText>(R.id.lastName).text.toString()
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()

        //initialize db
        myRef.child(userId).child("lastName").setValue(text)
        val myIntent = Intent(this, profileCustomer::class.java)
        startActivity(myIntent);
    }

}

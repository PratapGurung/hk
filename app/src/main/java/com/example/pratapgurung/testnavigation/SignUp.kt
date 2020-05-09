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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SignUp : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        val userTypeRadioBtn = findViewById<RadioGroup>(R.id.userType)

        //inflate address, city, zipcode for agents only
        userTypeRadioBtn.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            if (radioButton.text.equals("Customer")) {
                val fragment: emptyView = emptyView()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment, fragment)
                    commit()
                }
            } else {
                val fragment: agentSignUp = agentSignUp()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment, fragment)
                    commit()
                }
            }
        })
    }

    /*
        this function will check the form and
        create new user with assigned value
     */
    fun signUpSubmit(view: View) {

        val userTypeRadioBtn = findViewById<RadioGroup>(R.id.userType)

        //get the text from widget
        val firstName = findViewById<EditText>(R.id.firstName).text.toString()
        val lastName = findViewById<EditText>(R.id.lastName).text.toString()
        val email = findViewById<EditText>(R.id.email_agent).text.toString()
        val phone = findViewById<EditText>(R.id.phoneNumber).text.toString()
        val userName = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()
        val confirmPass = findViewById<EditText>(R.id.confirmPassword).text.toString()

        // validate and submit the data to corresponding user type
        if (validateEditTextData(
                firstName,
                lastName,
                email,
                phone,
                userName,
                password,
                confirmPass
            )
        ) {

            var radioBtnid: Int = userTypeRadioBtn.checkedRadioButtonId
            if (radioBtnid != -1) { // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio: RadioButton = findViewById(radioBtnid)
                val userRef = database.getReference().child("user").child(userName)
                // Read from the database
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        //if userExist don't submit to db
                        if(dataSnapshot.exists()){
                            displayToast("Username already taken!!!")
                        }
                        else{
                            //if user type is customer, add the user in customer table
                            if (radio.text.equals("Customer")) {
                                val custRef = database.getReference().child("user")
                                custRef.push().setValue(userName)
                                val newCustomer = Customer(
                                    firstName,
                                    lastName,
                                    email,
                                    phone,
                                    userName,
                                    password,
                                    radio.text.toString()
                                )
                                custRef.child(userName).setValue(newCustomer)
                                displayToast("User Sucessfully Created!!!")
                                val myIntent = Intent(this@SignUp, loginActivity::class.java)
                                startActivity(myIntent);

                            }
                            //else add the user in agents table
                            else {
                                val agentRef = database.getReference().child("user")
                                agentRef.push().setValue(userName)

                                //additional widgets for agents
                                val address = findViewById<EditText>(R.id.address).text.toString()
                                val city = findViewById<EditText>(R.id.city).text.toString()
                                val zipcode = findViewById<EditText>(R.id.agentzipcode).text.toString()
                                val states = findViewById<EditText>(R.id.states).text.toString()
                                //validate additional data for agent
                                if(address.isBlank() ||city.isBlank() || zipcode.isBlank() ||states.isBlank()){
                                    displayToast("Please Fill up all the text fields !!")
                                }
                                else{
                                    val newAgent = Agent(
                                        firstName,
                                        lastName,
                                        email,
                                        phone,address,city,states, zipcode,
                                        userName,
                                        password,
                                        radio.text.toString()
                                    )
                                    agentRef.child(userName).setValue(newAgent)
                                    displayToast("User Sucessfully Created!!!")
                                    val myIntent = Intent(this@SignUp, loginActivity::class.java)
                                    startActivity(myIntent);
                                }


                            }
                        }

                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })


            } else {
                // If no radio button checked in this radio group
                displayToast(" Please select user type!!")
            }
        }
    }

    /*
        this funciton will display toast on the screen
        with passed parameter
     */
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

    /*
           this function will validate the form submission and return boolean
           value either true or false
     */
    fun validateEditTextData(
        firstName: String, lastName: String, email: String,
        phone: String, userName: String, password: String, confirmPass: String
    ): Boolean {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() ||
            phone.isBlank() || userName.isBlank() || password.isBlank() || confirmPass.isBlank()
        ) {
            displayToast("Please Fill up all the text fields !!")
            return false
        } else if (!password.equals(confirmPass)) {
            displayToast("password does not Match")
            return false
        }
        return true
    }
}

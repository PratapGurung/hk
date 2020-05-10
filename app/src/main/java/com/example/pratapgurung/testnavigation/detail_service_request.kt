package com.example.pratapgurung.testnavigation


import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.round


class detail_service_request : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()

    private var custId: String? = null
    private var state: kotlin.String? = null
    private var zipcode: kotlin.String? = null
    private var city: String? = null
    private var fee: kotlin.String? = null
    private var address: kotlin.String? = null
    private var desc: kotlin.String? = null
    private var rDate: kotlin.String? = null
    private var estDeadline: kotlin.String? = null
    private var estHr: kotlin.String? = null
    private var sType: kotlin.String? = null

    private var orderId = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_service_request)

        var intent = getIntent()
        orderId = intent.extras["order"].toString()

        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        val userType = settings.getString("userType", "").toString() // get the logged in user type

        //create database reference to order to get the order details
        var orderRef = database.getReference().child("orders").child(orderId)

        //retrieve object from database
        orderRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.
                address = data.child("address").value.toString()
                fee = data.child("rate").value.toString()
                desc = data.child("description").value.toString()
                city = data.child("city").value.toString()
                state = data.child("state").value.toString()
                zipcode = data.child("zipCode").value.toString()
                rDate = data.child("requestDate").value.toString()
                estDeadline = data.child("completeByDate").value.toString()
                estHr = data.child("serviceHour").value.toString()
                sType = data.child("serviceType").value.toString()
                val agentName = data.child("acceptedby").value.toString()
                val requesteBy = data.child("requestedby").value.toString()
                val status = data.child("status").value.toString()

                //displayToast(agentName.toString())

                //widgets
                val statusView = findViewById<TextView>(R.id.status)
                val addLineView = findViewById<TextView>(R.id.address)
                val cityView = findViewById<TextView>(R.id.city)
                val stateView = findViewById<TextView>(R.id.state)
                val zipCodeView = findViewById<TextView>(R.id.zicode)
                val requestDateView = findViewById<TextView>(R.id.requestDate)
                val estDeadlineView = findViewById<TextView>(R.id.estDeadline)
                val estHourView = findViewById<TextView>(R.id.estHrs)
                val sFeeView = findViewById<TextView>(R.id.serviceFee)
                val descView = findViewById<TextView>(R.id.description)
                val serviceTypeView = findViewById<TextView>(R.id.serviceType)

                statusView.text = status
                addLineView.text = address
                cityView.text = city
                stateView.text = state
                zipCodeView.text = zipcode
                requestDateView.text = rDate
                estDeadlineView.text = estDeadline
                estHourView.text = estHr
                sFeeView.text = fee
                descView.text = desc
                serviceTypeView.text = sType

                //set the status
                if (status.equals("pending")) {
                    val fragment: emptyView = emptyView()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment, fragment)
                        commit()
                    }

                } else if (status.equals("completed")){
                    val fragment: status_accepted = status_accepted()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment, fragment)
                        commit()
                    }
                    if(userType.toLowerCase().equals("customer")){
                        userInfo(agentName)
                    }
                    else{
                        userInfo(requesteBy)
                    }

                }
                else if(status.equals("accepted")){
                    val accepted: status_accepted = status_accepted()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment, accepted)
                        commit()
                    }
                    val completed: status_completed = status_completed()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment1, completed)
                        commit()
                    }
                    if(userType.toLowerCase().equals("customer")){
                        userInfo(agentName)
                    }
                    else{
                        userInfo(requesteBy)
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        });




    }

    fun userInfo(agentName: String) {

        //now change the reference to user to get the agent information
        var userRef = database.getReference().child("user").child(agentName.toString())
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {
                //get value from database

                val firstName = data.child("firstName").value.toString()
                val lastName = data.child("lastName").value.toString()
                val email = data.child("email").value.toString()
                val phone = data.child("phone").value.toString()

                //get the widgets
                val nameView = findViewById<TextView>(R.id.userName)
                val phoneView = findViewById<TextView>(R.id.phone_agent)
                val emailView = findViewById<TextView>(R.id.email_agent)

                //now set the widgent text value to one from database
                nameView.text = firstName + " " + lastName
                phoneView.text = phone
                emailView.text = email


            }
            override fun onCancelled(p0: DatabaseError) {}
        });
    }

    fun rateService(view: View){

        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        val userType = settings.getString("userType", "").toString() // get the logged in user's usertype

        //create database reference to order to get the order details
        var orderRef = database.getReference().child("orders").child(orderId)

        //get the agent id of order
        orderRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                //get value from database
                //change the order status to completed from accepted
                orderRef.child("status").setValue("completed")
                //rate the respective userType
                if(userType.toLowerCase().equals("service provider")){
                    val userId = data.child("requestedby").value.toString()
                    rate(userId)
                }
                else{
                    val userId = data.child("acceptedby").value.toString()
                    rate(userId)
                }


            }
            override fun onCancelled(p0: DatabaseError) {}
        });

    }
    fun rate(userId: String){
        //get the agent's ratings value
        var ratings = 0.0
        //now read from user table from db
        var agentRef = database.getReference().child("user").child(userId)
        agentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                //get old ratign value from database
                ratings = data.child("ratings").value.toString().toDouble()

                //submit the new ratings
                val ratingNum = findViewById<RatingBar>(R.id.ratingBar).rating //get the ratings from widget
                var newratings =  ( ratingNum + ratings ) / 2  //new ratings
                newratings = (round(newratings * 10) / 10 )
                agentRef.child("ratings").setValue(newratings) //submit to the db

                displayToast("Successfully rated!!!")
                val myIntent = Intent(this@detail_service_request, RequestStatusCustomer::class.java)
                startActivity(myIntent)
            }
            override fun onCancelled(p0: DatabaseError) {}
        });
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
}

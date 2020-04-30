package com.example.pratapgurung.testnavigation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class detail_service_request_agent : AppCompatActivity() {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_service_request)

        var intent = getIntent()
        val orderId = intent.extras["order"].toString()
        //Toast.makeText(getApplicationContext(), "Selected item at position: " + orderId, Toast.LENGTH_LONG).show();
        //create database reference
        val myRef = database.getReference().child("orders").child(orderId)

        //retrieve object from database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.
                address = data.child("address").value.toString()
                custId = data.child("requestedby").value.toString()
                fee = data.child("rate").value.toString()
                desc = data.child("description").value.toString()
                city = data.child("city").value.toString()
                state = data.child("state").value.toString()
                zipcode = data.child("zipCode").value.toString()
                rDate = data.child("requestedDate").value.toString()
                estDeadline = data.child("completeDate").value.toString()
                estHr = data.child("serviceHour").value.toString()
                sType = data.child("servicetype").value.toString()

                //widgets
                val addLineView = findViewById<TextView>(R.id.address)
                val cityView = findViewById<TextView>(R.id.city)
                val stateView = findViewById<TextView>(R.id.state)
                val zipCodeView = findViewById<TextView>(R.id.zicode)
                val custNameView = findViewById<TextView>(R.id.customerName)
                val requestDateView = findViewById<TextView>(R.id.requestDate)
                val estDeadlineView = findViewById<TextView>(R.id.estDeadline)
                val estHourView = findViewById<TextView>(R.id.estHrs)
                val sFeeView = findViewById<TextView>(R.id.serviceFee)
                val descView = findViewById<TextView>(R.id.descriptions)
                val serviceTypeView = findViewById<TextView>(R.id.descriptions)

                addLineView.text = address
                cityView.text = city
                stateView.text = state
                zipCodeView.text = zipcode
                custNameView.text = custId
                requestDateView.text = rDate
                estDeadlineView.text = estDeadline
                estHourView.text = estHr
                sFeeView.text = fee
                descView.text = desc
                serviceTypeView.text = sType

            }


            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        });

    }
    fun acceptRequestOrder(view:View){

    }
}

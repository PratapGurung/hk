package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.database.*


class Earning_Details : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earning__details)

        var intent = getIntent()
        val earnings_constraint = intent.extras["earning_contraint"].toString()
        findViewById<TextView>(R.id.header).text = earnings_constraint


        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        val userId = settings.getString("userId", "").toString() // get the userName

        //connect to the database and get the orders accepted by this service provider
        val orders  = database.getReference().child("orders")
        val query: Query = orders.orderByChild("acceptedby").equalTo(userId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) { // dataSnapshot is the "issue" node with all children with id 0
                    var earnings = 0.0
                    for (issue in dataSnapshot.children) { // do something with the individual "issues"
                            var rate = issue.child("rate").value.toString().toDouble()
                            var hrs = issue.child("serviceHour").value.toString().toDouble()

                            earnings += rate * hrs
                    }

                    findViewById<TextView>(R.id.earningsTotal).text = earnings.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    override fun onStart() {
        super.onStart();

        val listview = findViewById<ListView>(R.id.earningList);
        val orderrlistItem = ArrayList<Order>()

        val orders  = database.getReference().child("orders")
        val query: Query = orders.orderByChild("acceptedby").equalTo("pratapgrg123")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataList: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.

                orderrlistItem.clear()
                for (data in dataList.children) {

                    val add = data.child("address").value.toString()
                    val estDeadline = data.child("completedByDate").value.toString()
                    val estHr = data.child("serviceHour").value.toString()
                    val status = data.child("status").value.toString()
                    val rate = data.child("rate").value.toString()
                    val timestamp = data.child("timestamp").value.toString().toLong()

                    val order = Order(
                        "", "", "", add, "",
                        "", "", estHr, "",
                        "", estDeadline, "",rate, status, timestamp
                    )
                    orderrlistItem.add(order)


                }
                //variable to hold sorted list
                var sortedOrderList  = ArrayList<Order>()
                //sort the list and store in variable
                orderrlistItem.sortedWith(compareBy<Order>({ it.timestamp }).reversed()).toCollection(sortedOrderList)
                //create array adapter
                val adapter = earning_list(this@Earning_Details, sortedOrderList)
                listview.setAdapter(adapter);
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        });

    }
}

package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.database.*

/*
    this activity display detailk view of earnings
    by constraints
 */
class Earning_Details : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earning__details)

        //get the content from actiivty
        var intent = getIntent()
        //get the earnings constraint type
        val earnings_constraint = intent.extras["earning_contraint"].toString()
        findViewById<TextView>(R.id.header).text = earnings_constraint


        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        val userId = settings.getString("userId", "").toString() // get the userName

        //connect to the database and get the orders accepted by this service provider
        val orders  = database.getReference().child("orders")
        val query: Query = orders.orderByChild("acceptedby").equalTo(userId)

        //run query on database to read orders
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) { // dataSnapshot is the "issue" node with all children with id 0
                    var earnings = 0.0

                    //calculate earning for user
                    for (issue in dataSnapshot.children) {
                        // do something with the individual "issues"
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

        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()

        //get the list view of earnings
        val listview = findViewById<ListView>(R.id.earningList);
        //creat instance of arraylist to hold orders
        val orderrlistItem = ArrayList<Order>()

        //create db reference
        val orders  = database.getReference().child("orders")
        val query: Query = orders.orderByChild("acceptedby").equalTo(userId)

        //run query on db refernce on each event listener return true
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataList: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.

                orderrlistItem.clear()
                for (data in dataList.children) {

                    val add = data.child("address").value.toString()
                    val estDeadline = data.child("completeByDate").value.toString()
                    val estHr = data.child("serviceHour").value.toString()
                    val status = data.child("status").value.toString()
                    val rate = data.child("rate").value.toString()
                    val timestamp = data.child("timestamp").value.toString().toLong()

                    //create a order instance
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

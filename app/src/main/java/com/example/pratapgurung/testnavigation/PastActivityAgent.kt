package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.database.*

/*
    this class creates past activity for agent
    this will display all the past request completed by the agent
 */
class PastActivityAgent : AppCompatActivity() {

    //create instance of database
    val database = FirebaseDatabase.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_agent)

    }

    override fun onStart() {
        super.onStart();

        //get the listView from view
        val listview = findViewById<ListView>(R.id.requestList);

        //create arraylist object
        val orderrlistItem = ArrayList<Order>()

        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        val userId = settings.getString("userId", "").toString()


        // get the db reference for order
        val orders  = database.getReference().child("orders")

        //create query to run on db
        var query: Query  = orders.orderByChild("acceptedby").equalTo(userId)

        // read the completed request
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataList: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.

                orderrlistItem.clear()
                for (data in dataList.children) {
                    val status = data.child("status").value.toString()
                    if(status.toLowerCase().equals("completed")){
                        val orderId = data.child("orderId").value.toString()
                        val add = data.child("address").value.toString()
                        val custId = data.child("requestedby").value.toString()
                        var rDate = data.child("requestDate").value.toString()
                        val desc = data.child("description").value.toString()
                        val city = data.child("city").value.toString()
                        val state = data.child("state").value.toString()
                        val zipcode = data.child("zipCode").value.toString()
                        val estDeadline = data.child("completeByDate").value.toString()
                        val estHr = data.child("serviceHour").value.toString()
                        val serviceId = data.child("serviceType").value.toString()
                        val timestamp = data.child("timestamp").value.toString().toLong()

                        val acceptedBy = data.child("acceptedby").value.toString()
                        val rate = data.child("rate").value.toString()

                        //create new object of order
                        val order = Order(
                            orderId, custId, serviceId, add, city,
                            state, zipcode, estHr, desc,
                            rDate, estDeadline, acceptedBy,rate, status, timestamp
                        )

                        //add the above created object to arraylist
                        orderrlistItem.add(order)
                    }

                }
                //variable to hold sorted list
                var sortedOrderList  = ArrayList<Order>()
                //sort the list and store in variable
                orderrlistItem.sortedWith(compareBy<Order>({ it.timestamp }).reversed()).toCollection(sortedOrderList)
                //create array adapter
                val adapter = orderList(this@PastActivityAgent, sortedOrderList)
                listview.setAdapter(adapter);

                listview.setOnItemClickListener { parent, view, position, id ->
                    val element = adapter.getItem(position)// The item that was clicked
                    //Toast.makeText(getApplicationContext(), "Selected item at position: " + element.orderId, Toast.LENGTH_LONG).show();
                    val intent =
                        Intent(this@PastActivityAgent, detail_service_request::class.java)
                    intent.putExtra("order", element.orderId)
                    startActivity(intent)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        });

    }
}

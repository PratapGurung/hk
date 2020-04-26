package com.example.pratapgurung.testnavigation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RequestStatus : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()

    //val orderlist = listOf<Order>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_status)

    }

    override fun onStart() {
        super.onStart();

        val listview = findViewById<ListView>(R.id.requestList);
        val orderrlistItem = ArrayList<Order>()

        // Most recent posts
        val myRef = database.getReference().child("orders").orderByChild("requestDate") // makes changes here it does not work yet
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataList: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.

                orderrlistItem.clear()
                for (data in dataList.children) {
                    val orderId = data.child("orderId").value.toString()
                    val add = data.child("address").value.toString()
                    val custId = data.child("customerId").value.toString()
                    var rDate = data.child("requestDate").value.toString()
                    val desc = data.child("description").value.toString()
                    val city = data.child("city").value.toString()
                    val state = data.child("state").value.toString()
                    val zipcode = data.child("zipCode").value.toString()
                    val estDeadline = data.child("completDate").value.toString()
                    val estHr = data.child("serviceHour").value.toString()
                    val serviceId = data.child("serviceId").value.toString()

                    val order =  Order(orderId, custId, serviceId, add, city,
                                        state, zipcode, estHr, desc,
                                        rDate, estDeadline, " ")
                    orderrlistItem.add(order)
                }
                //val adapter = orderList(this@RequestStatus, orderrlistItem)
                val adapter = orderList(this@RequestStatus, orderrlistItem)
                listview.setAdapter(adapter);

                listview.setOnItemClickListener { parent, view, position, id ->
                    val element = adapter.getItem(position)// The item that was clicked
                    //Toast.makeText(getApplicationContext(), "Selected item at position: " + element.orderId, Toast.LENGTH_LONG).show();
                    val intent = Intent(this@RequestStatus, detail_service_request::class.java)
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

    fun orderDetail(view: View){
        val myIntent = Intent(this, detail_service_request::class.java)
        //myIntent.putExtra("message", "Service Request Sucessfully Submitted!!!")
        startActivity(myIntent);
    }
}

package com.example.pratapgurung.testnavigation

import android.R.id.message
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*


class MainActivity_Agent : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    // Write a message to the database
    val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main__agent)
        setSupportActionBar(toolbar)
        FirebaseApp.initializeApp(this);

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)


        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the navigationView
        val navigationView : NavigationView  = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.username) //get the userName view
        val ratings : RatingBar = headerView.findViewById(R.id.ratingBar) //get the ratings bar
        navUsername.text = settings.getString("Username", "").toString() //set the user to current logged in username
        ratings.rating = settings.getString("ratings", "").toString().toFloat()

    }

    override fun onStart() {
        super.onStart();

        val listview = findViewById<ListView>(R.id.earningList);
        val orderrlistItem = ArrayList<Order>()

        // Most recent posts
        val orders  = database.getReference().child("orders")
        val query: Query = orders.orderByChild("status").equalTo("pending")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataList: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.

                orderrlistItem.clear()
                for (data in dataList.children) {
                    val orderId = data.child("orderId").value.toString()
                    val add = data.child("address").value.toString()
                    val custId = data.child("requestedby").value.toString()
                    var rDate = data.child("requestedDate").value.toString()
                    val desc = data.child("description").value.toString()
                    val city = data.child("city").value.toString()
                    val state = data.child("state").value.toString()
                    val zipcode = data.child("zipCode").value.toString()
                    val estDeadline = data.child("completeDate").value.toString()
                    val estHr = data.child("serviceHour").value.toString()
                    val serviceId = data.child("serviceType").value.toString()

                    val order =  Order(orderId, custId, serviceId, add, city,
                        state, zipcode, estHr, desc,
                        rDate, estDeadline, " ")
                    orderrlistItem.add(order)
                }
                //val adapter = orderList(this@RequestStatus, orderrlistItem)
                val adapter = orderList(this@MainActivity_Agent, orderrlistItem)
                listview.setAdapter(adapter);

                listview.setOnItemClickListener { parent, view, position, id ->
                    val element = adapter.getItem(position)// The item that was clicked
                    //Toast.makeText(getApplicationContext(), "Selected item at position: " + element.orderId, Toast.LENGTH_LONG).show();
                    val intent = Intent(this@MainActivity_Agent, detail_service_request_agent::class.java)
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
        val myIntent = Intent(this, detail_service_request_agent::class.java)
        //myIntent.putExtra("message", "Service Request Sucessfully Submitted!!!")
        startActivity(myIntent);
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.nav_check_request_requests -> {
                val myIntent = Intent(this, RequestStatusAgent::class.java)
                startActivity(myIntent);
            }

            R.id.nav_past_request -> {
                val myIntent = Intent(this, PastActivity::class.java)
                startActivity(myIntent);
            }

            R.id.nav_earning -> {
                val myIntent = Intent(this, earnings::class.java)
                startActivity(myIntent);
            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    //
    fun openProfile(view: View) {
        val myIntent = Intent(this, profile::class.java)
        startActivity(myIntent);

    }

    fun displayToast(msg: String) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        // val toast = Toast.makeText(context, message, duration)
        val view = toast.view
        //Gets the actual oval background of the Toast then sets the colour filter
        view.background.setColorFilter(Color.parseColor("#19bd60"), PorterDuff.Mode.SRC_IN)
        //Gets the TextView from the Toast so it can be editted
        val text = view.findViewById<TextView>(message)
        text.setTextColor(Color.parseColor("#f7f7f7"))
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F);
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }



}

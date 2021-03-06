package com.example.pratapgurung.testnavigation

import android.R.id.message
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import android.widget.ListView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*

/*
    this is main activity or home page of service provider
    this activity will display recent posting for service request and  controls the navigation
 */
class MainActivity_Agent : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    // create database instance
    val database = FirebaseDatabase.getInstance()

    /*
        overridden on create function and is called every time activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main__agent)
        setSupportActionBar(toolbar)
        FirebaseApp.initializeApp(this);

        /*
            toggle bar for navigation
         */
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
        val navUsername : TextView = headerView.findViewById(R.id.usernameView) //get the userName view
        val ratings : RatingBar = headerView.findViewById(R.id.ratingBar) //get the ratings bar

        //get the current logged in user info from shared preference and set it to view
        navUsername.text = settings.getString("Username", "").toString()
        ratings.rating = settings.getString("ratings", "").toString().toFloat()

    }

    /*
           overridden on create function and is called every time activity is started
           and controls the home view of agent
       */
    override fun onStart() {
        super.onStart();

        //get the list view
        val listview = findViewById<ListView>(R.id.earningList);

        //initialiaze the array list
        val orderrlistItem = ArrayList<Order>()

        // read the Most recent posts from database
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
                    var rDate = data.child("requestDate").value.toString()
                    val desc = data.child("description").value.toString()
                    val city = data.child("city").value.toString()
                    val state = data.child("state").value.toString()
                    val zipcode = data.child("zipCode").value.toString()
                    val estDeadline = data.child("completeDate").value.toString()
                    val estHr = data.child("serviceHour").value.toString()
                    val serviceId = data.child("serviceType").value.toString()
                    val status = data.child("status").value.toString()
                    val rate = data.child("rate").value.toString()
                    val timestamp = data.child("timestamp").value.toString().toLong()
                    val order =  Order(orderId, custId, serviceId, add, city,
                        state, zipcode, estHr, desc,
                        rDate, estDeadline, " ", rate, status, timestamp)
                    orderrlistItem.add(order)
                }
                //variable to hold sorted list
                var sortedOrderList  = ArrayList<Order>()
                //sort the list and store in variable
                orderrlistItem.sortedWith(compareBy<Order>({ it.timestamp }).reversed()).toCollection(sortedOrderList)
                //create array adapter
                val adapter = orderList(this@MainActivity_Agent, sortedOrderList)
                listview.setAdapter(adapter);

                //set  onclick listener to each item of list view
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

    /*
        this function controls navigation items and is
        called when item from navation is selected or clicked
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.nav_check_request_requests -> {
                val myIntent = Intent(this, RequestStatusAgent::class.java)
                startActivity(myIntent);
            }

            R.id.nav_past_request -> {
                val myIntent = Intent(this, PastActivityAgent::class.java)
                startActivity(myIntent);
            }

            R.id.nav_earning -> {
                val myIntent = Intent(this, earnings::class.java)
                startActivity(myIntent);
            }

            R.id.nav_log_out -> {
                val settings: SharedPreferences = this.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE)
                settings.edit().clear().commit()
                displayToast("You logged out!!! See you Again");//leave log out message
                val myIntent = Intent(this, loginActivity::class.java)
                startActivity(myIntent);
            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /*
        called when list item is clicked and starts new activity called detail-service-request-agent
   */
    fun orderDetail(view: View){
        val myIntent = Intent(this, detail_service_request_agent::class.java)
        startActivity(myIntent);
    }

    //called when user profile image from navigation is pressed
    //starts new activity called proileAgent
    fun openProfile(view: View) {
        val myIntent = Intent(this, profileAgent::class.java)
        startActivity(myIntent);

    }

    //helper function to display toast message
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

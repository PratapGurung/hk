package com.example.pratapgurung.testnavigation

import android.R.id.message
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
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
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.File
import java.time.LocalDate
import java.util.*

/*
    this is main activity or home page of customer

 */
class MainActivity_Customer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener{

    // create database instance
    val database = FirebaseDatabase.getInstance()
    var datepicker: Button? = null //variable for calendar

    /*
        overridden on create function and is called every time activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        spinnerSetup()

        //get the username and ratings from shared reference and set it on navigation bar
        setUserNameNrating()

        //show the profile image of user
        //get the profile image from firebase storage
        retrievePicture()
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
        retreives profile picture and put in navigation
     */
    fun retrievePicture(){
        //get the profile image from firebase storage
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val riversRef = storageReference.child("images/testImage" )
        val localFile: File = File.createTempFile("images", "jpg")
        riversRef.getFile(localFile)
            .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                // Successfully downloaded data to local file
                // ...
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
            }).addOnFailureListener(OnFailureListener {
                // Handle failed download
                // ...
            })
    }

    /*
        gets the username and ratings from shared reference (local memory)
     */
    fun setUserNameNrating(){
        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the navigationView
        val navigationView : NavigationView  = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.usernameView) //get the userName view
        val ratings : RatingBar = headerView.findViewById(R.id.ratingBar) //get the ratings bar

        //get the current logged in user info from shared preference and set it to view
        navUsername.text = settings.getString("Username", "").toString() //set the user to current logged in username
        ratings.rating = settings.getString("ratings", "5").toString().toFloat()
    }


    /*
       this function controls navigation items and is
       called when item from navation is selected or clicked
    */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {


            R.id.nav_check_request_requests -> {
                val myIntent = Intent(this, RequestStatusCustomer::class.java)
                startActivity(myIntent);
            }
            R.id.nav_past_request -> {
                val myIntent = Intent(this, PastActivityCustomer::class.java)
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

    //called when user profile image from navigation is pressed
    //    //starts new activity called proileCustomer
    fun openProfile(view: View) {
        val myIntent = Intent(this, profileCustomer::class.java)
        startActivity(myIntent);

    }

    //help function to create spinner
    fun spinnerSetup(){

        var mySpinner = findViewById<Spinner>(R.id.mainSpinner1);
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val myAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.serviceNames)
        )
        // Set layout to use when the list of choices appear
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set Adapter to Spinner
        mySpinner.setAdapter(myAdapter);

        var spinner2 = findViewById<Spinner>(R.id.states);
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val statesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.states)
        )
        // Set layout to use when the list of choices appear
        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set Adapter to Spinner
        spinner2.setAdapter(statesAdapter);
    }

    /*
     * reset all the data
     * in view
     */
    fun resetData(view: View) {
        //spinner
        val serviceType = findViewById<Spinner>(R.id.mainSpinner1)
        serviceType.setSelection(0)
        //addressline
        val addLine = findViewById<EditText>(R.id.mainAddLine)
        addLine.setText("")
        //city
        val city = findViewById<EditText>(R.id.mainCity)
        city.setText("")
        //states
        val spinnerStates = findViewById<Spinner>(R.id.states)
        spinnerStates.setSelection(0)
        //Zipcode
        val zipCode = findViewById<EditText>(R.id.mainZipcode)
        zipCode.setText("");
        //estimated hours
        val estHour = findViewById<EditText>(R.id.estHour)
        estHour.setText("");
        //estimated hours
        val desc = findViewById<EditText>(R.id.descriptions)
        desc.setText("");

        val rate = findViewById<EditText>(R.id.rate)
        rate.setText("");
        val deadlinedate = findViewById<Button>(R.id.datepicker)
        deadlinedate.setText("");

    }

    /*
     get all the information entered by user and create new instance
      of order and submit that order with order id to the database

     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun requestSubmit(view: View) {
        //widgets
        val serviceType = findViewById<Spinner>(R.id.mainSpinner1)//spinner
        val spinnerStates = findViewById<Spinner>(R.id.states)


        //widget text
        val stType = findViewById<Spinner>(R.id.mainSpinner1).selectedItem.toString()
        val addLineText = findViewById<EditText>(R.id.mainAddLine).text.toString()
        val cityTxt = findViewById<EditText>(R.id.mainCity).text.toString()
        val spStateTxt = findViewById<Spinner>(R.id.states).selectedItem.toString()
        val zipTxt = findViewById<EditText>(R.id.mainZipcode).text.toString()
        val estHrtxt = findViewById<EditText>(R.id.estHour).text.toString()
        val descTxt = findViewById<EditText>(R.id.descriptions).text.toString()
        val rateTxt = findViewById<EditText>(R.id.rate).text.toString()
        val dateTxt = findViewById<Button>(R.id.datepicker).text.toString()


        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()

        //get the database reference
        val myRef = database.getReference().child("orders")

        //validate data before submitting
        if (serviceType.getSelectedItem().toString().trim().equals("Service Type")) {
            displayToast("Please Select service type")
        } else if (spinnerStates.getSelectedItem().toString().trim().equals("Select States")) {
            displayToast("Please Select States")
        }
        else if (dateTxt.isBlank() ) {
            displayToast("Please Select Date!!")
        }
        //validate editText
        else if(addLineText.isBlank() || cityTxt.isBlank() || spStateTxt.isBlank() || zipTxt.isBlank() ||estHrtxt.isBlank() || descTxt.isBlank()|| rateTxt.isBlank()){
            displayToast("Please Fill all the required forms!!")
        }
        else {
            //now write to db and start new activity only iff all value are fullfilled

            // Creating new user node, which returns the unique key value
            // new user node would be /users/$userid/
            val orderId = myRef.push().getKey().toString()
            val date = LocalDate.now().toString()

            val currentTimestamp = System.currentTimeMillis()
            val newOrder = Order(
                orderId, userId, stType, addLineText, cityTxt, spStateTxt,
                zipTxt, estHrtxt, descTxt, date, dateTxt, "", rateTxt, "pending", currentTimestamp
            )

            myRef.child(orderId).setValue(newOrder)
            val myIntent = Intent(this, MainActivity_Customer::class.java)
            startActivity(myIntent);
            displayToast("Service Request Sucessfully Submitted!!!")
        }


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

    //helper function to create date picker
    fun getDatePicker(view: View){
        datepicker = findViewById<Button>(R.id.datepicker)
        datepicker!!.setOnClickListener(View.OnClickListener { showDatePickerDialog() })
    }

    //function that will display date picker dialog box
    fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    //on date set change the text of datepicker view
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$month/$dayOfMonth/$year"
        datepicker!!.setText(date)
    }
}

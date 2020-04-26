package com.example.pratapgurung.testnavigation

import android.R.id.message
import android.app.DatePickerDialog
import android.content.Intent
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
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener{
    // Write a message to the database
    val database = FirebaseDatabase.getInstance()
    var datepicker: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        spinnerSetup()

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
            R.id.nav_message -> {
                // Handle the camera action
            }
            R.id.nav_agents -> {
                val myIntent = Intent(this, CheckAgents::class.java)
                startActivity(myIntent);
            }
            R.id.nav_requests -> {
                val myIntent = Intent(this, RequestStatus::class.java)
                startActivity(myIntent);
            }
            R.id.nav_past_activity -> {
                val myIntent = Intent(this, PastActivity::class.java)
                startActivity(myIntent);
            }
            R.id.nav_help -> {
                val myIntent = Intent(this, Help::class.java)
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

        var spinner2 = findViewById<Spinner>(R.id.spinnerStates);
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
     *
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
        val spinnerStates = findViewById<Spinner>(R.id.spinnerStates)
        spinnerStates.setSelection(0)
        //Zipcode
        val zipCode = findViewById<EditText>(R.id.mainZipcode)
        zipCode.setText("");
        //estimated hours
        val estHour = findViewById<EditText>(R.id.estHour)
        estHour.setText("");
        //estimated hours
        val desc = findViewById<EditText>(R.id.description)
        desc.setText("");

        val rate = findViewById<EditText>(R.id.rate)
        rate.setText("");
        val deadlinedate = findViewById<Button>(R.id.datepicker)
        deadlinedate.setText("");

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun requestSubmit(view: View) {
        //widgets
        val serviceType = findViewById<Spinner>(R.id.mainSpinner1)//spinner
        val addLine = findViewById<EditText>(R.id.mainAddLine)
        val city = findViewById<EditText>(R.id.mainCity)
        val spinnerStates = findViewById<Spinner>(R.id.spinnerStates)
        val zipCode = findViewById<EditText>(R.id.mainZipcode)
        val estHour = findViewById<EditText>(R.id.estHour)
        val desc = findViewById<EditText>(R.id.description)
        val deadlinedate = findViewById<Button>(R.id.datepicker)
        val rate = findViewById<EditText>(R.id.rate)

        //widget text
        val stType = serviceType.selectedItem.toString()
        val addLineText = addLine.text.toString()
        val cityTxt = city.text.toString()
        val spStateTxt = spinnerStates.selectedItem.toString()
        val zipTxt = zipCode.text.toString()
        val estHrtxt = estHour.text.toString()
        val descTxt = desc.text.toString()
        val rateTxt = rate.text.toString()
        val dateTxt = deadlinedate.text.toString()



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
            val newOrder = Order(
                orderId, "", " ", addLineText, cityTxt, spStateTxt,
                zipTxt, estHrtxt, descTxt, date, dateTxt, "", rateTxt
            )

            myRef.child(orderId).setValue(newOrder)
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent);
            displayToast("Service Request Sucessfully Submitted!!!")
        }


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

    fun getDatePicker(view: View){
        datepicker = findViewById<Button>(R.id.datepicker)
        datepicker!!.setOnClickListener(View.OnClickListener { showDatePickerDialog() })
    }

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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$month/$dayOfMonth/$year"
        datepicker!!.setText(date)
    }
}
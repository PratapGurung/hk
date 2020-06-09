package com.example.pratapgurung.testnavigation

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*


/*
    Activity Description: this activity create profile for logged in customer
                           and gets user information from database
 */

class profileCustomer : AppCompatActivity() {
    //shared preference variable
    var settings:SharedPreferences? = null
    var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_cust)

        // initializae database instance
        val database = FirebaseDatabase.getInstance()

        //get the sharedpreference
        val settings = getSharedPreferences("UserInfo", 0)
        //get the current logged in user info
        val userId = settings.getString("userId", "").toString()

        //create reference to current logged in user in database
        val myRef = database.getReference().child("user").child(userId)

        //get all the required information from db
        myRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                //To change body of created functions use File | Settings | File Templates.

                val fName = p0.child("firstName").value.toString()
                val lName = p0.child("lastName").value.toString()
                //var pw = p0.child("password").value.toString()
                //val re = Regex(".")
               // pw = re.replace(pw, "*")
                val phNumber = p0.child("phone").value.toString()
                val em = p0.child("email").value.toString()

                //display the user information on view
                findViewById<TextView>(R.id.username).text = userId
                findViewById<TextView>(R.id.clientFirstNameView).text = fName
                findViewById<TextView>(R.id.clientLastNameView).text = lName
               // findViewById<TextView>(R.id.clientPassWordView).text = pw
                findViewById<TextView>(R.id.clientEmailView).text = em
                findViewById<TextView>(R.id.clientPhoneNumView).text = phNumber

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.

            }
        })

        retrievePicture();

    }

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
        this method will be called when user pressed firstName view
        and start new activity called firstName
     */
    fun setFirstName(view : View){
        val myIntent = Intent(this, firstName::class.java)
        startActivity(myIntent)
    }

    /*
        this method will be called when user pressed LastName view
        and start new activity called secondName
     */
    fun setSecondName(view : View){
        val myIntent = Intent(this, secondName::class.java)
        startActivity(myIntent)

    }

    /*
        this method will be called when user pressed password view
        and start new activity called password
     */
    fun setPassword(view : View){
        val myIntent = Intent(this, password::class.java)
        startActivity(myIntent)

    }

    /*
        this method will be called when user pressed phonenumber view
        and start new activity called phonenumber
     */
    fun setPhoneNumber(view : View){
        val myIntent = Intent(this, phoneNumber::class.java)
        startActivity(myIntent)

    }
    /*
            this method will be called when user pressed email view
            and start new activity called email
     */
    fun setEmail(view : View){
        val myIntent = Intent(this, email::class.java)
        startActivity(myIntent)

    }

    fun uploadPicture(view: View){
        choosePicture()
    }

    fun choosePicture(){
        val intent =  Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==1 && resultCode == Activity.RESULT_OK && data != null &&  data.data != null){
            imageUri = data.data
            findViewById<ImageView>(R.id.imageView).setImageURI(imageUri)
            uploadPicture()
        }
    }

    fun uploadPicture(){
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference
        val randomKey = UUID.randomUUID().toString()
        val riversRef = storageReference.child("images/" + randomKey)

        riversRef.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // Get a URL to the uploaded content
                Snackbar.make(findViewById<View>(android.R.id.content), "image uploaded", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                // Handle unsuccessful uploads
                // ...
            }
    }
}

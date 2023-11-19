package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.firebase.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    //Auth
    private lateinit var auth: FirebaseAuth

    //Realtime Databse
    private lateinit var myDatabase: FirebaseDatabase
    private lateinit var myReference: DatabaseReference

    //Firestore Database
    private val db = Firebase.firestore

    private var txtName: EditText? = null
    private var txtCity: EditText? = null
    private var txtCountry: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        myDatabase = FirebaseDatabase.getInstance()
        myReference = myDatabase.getReference("My Users")

        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnCreate = findViewById<Button>(R.id.btnCreate)

        txtName = findViewById(R.id.txtName)
        txtCity = findViewById(R.id.txtCity)
        txtCountry = findViewById(R.id.txtCountry)

        btnCreate.setOnClickListener {
            if (txtEmail.text.trim().isNotEmpty() || txtPassword.text.trim().isNotEmpty()) {
                createUser(txtEmail.text.trim().toString(), txtPassword.text.trim().toString())
            } else {
                Snackbar.make(
                    btnCreate,
                    "Check your username and password then try again!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    sendData(
                        User(
                            txtName!!.text.toString().trim(),
                            txtCity!!.text.toString().trim(),
                            txtCountry!!.text.toString().trim(),
                        )
                    )
                    newUser()
                    startActivity(Intent(this, WelcomeActivity::class.java))
                } else {
                    Snackbar.make(
                        findViewById(R.id.btnCreate),
                        "Authentication failed. ${task.exception}", Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun newUser() {
        val user = hashMapOf(
            "Name" to txtName!!.text.toString().trim(),
            "City" to txtCity!!.text.toString().trim(),
            "Country" to txtCountry!!.text.toString().trim(),
        )
        db.collection("Users")
            .add(user)
            .addOnSuccessListener { documentRef ->
                Log.d("", "Document added with ID: ${documentRef.id}")
            }
            .addOnFailureListener { e ->
                Log.d("", "Error adding document", e)
            }
    }

    private fun sendData(user: User) {
        if (user.name.isNotEmpty() && user.city.isNotEmpty() && user.country.isNotEmpty()) {
            val id = myReference.push().key
            myReference.child(id!!).setValue(user)
        }
    }
}
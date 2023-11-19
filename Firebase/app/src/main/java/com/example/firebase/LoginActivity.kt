package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        val txtEmailAddress = findViewById<EditText>(R.id.txtEmailAddress)
        val txtPass = findViewById<EditText>(R.id.txtPass)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            login(txtEmailAddress.text.trim().toString(), txtPass.text.trim().toString())
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                    startActivity(Intent(this, ServiceActivity::class.java))
                else {
                    Snackbar.make(
                        findViewById(R.id.btnLogin), "Enter a valid username or password", Snackbar
                            .LENGTH_LONG
                    ).show()
                }
            }
    }
}
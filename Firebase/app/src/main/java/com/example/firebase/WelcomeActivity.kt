package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val btnGotoPortal = findViewById<Button>(R.id.btnGotoPortal)
        btnGotoPortal.setOnClickListener {
            startActivity(Intent(this, ServiceActivity::class.java))
        }
    }
}
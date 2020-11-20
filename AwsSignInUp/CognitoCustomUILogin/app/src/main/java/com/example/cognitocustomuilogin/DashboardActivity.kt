package com.example.cognitocustomuilogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DashboardActivity : AppCompatActivity() {

    private lateinit var txt_username : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        txt_username = findViewById(R.id.txt_user)
        txt_username.text = UserDetails.username
    }
}
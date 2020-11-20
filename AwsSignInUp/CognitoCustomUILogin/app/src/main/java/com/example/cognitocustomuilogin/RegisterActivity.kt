package com.example.cognitocustomuilogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {

    private lateinit var txt_username : EditText
    private lateinit var txt_email : EditText
    private lateinit var txt_pass : EditText
    private lateinit var btn_signup : Button
    private lateinit var txt_verify : TextView
    private lateinit var txt_go_log_in : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txt_username = findViewById(R.id.edt_usernme)
        txt_email = findViewById(R.id.edt_email)
        txt_pass = findViewById(R.id.edt_password)
        txt_go_log_in = findViewById(R.id.txt_go_login)
        btn_signup = findViewById(R.id.btn_signup)
        txt_verify = findViewById(R.id.goto_verify)

        txt_go_log_in.visibility = View.VISIBLE

        registerUser()
        txt_verify.setOnClickListener {
            val intent = Intent(this, VerifyActivity::class.java)
            startActivity(intent)
        }

        txt_go_log_in.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun registerUser(){

        //Create a CognitoUserAttributes object and add user attribute
        val userAttributes = CognitoUserAttributes()

        val signupCallback = object : SignUpHandler{
            override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                Log.d(TAG, "sign up success is confirmed.....$signUpResult")

                if (!signUpResult!!.userConfirmed){
                    Log.d(TAG, "sign up success not confirmed ..... verification code sent to ${signUpResult.codeDeliveryDetails.destination}")
                    Toast.makeText(this@RegisterActivity, "Verification code has been sent to your email address.", Toast.LENGTH_LONG).show()
                    txt_verify.visibility = View.VISIBLE
                    txt_email.setText("")
                    txt_username.setText("")
                    txt_pass.setText("")
                    txt_go_log_in.visibility = View.INVISIBLE
                }
                else{
                    Log.d(TAG, "sign up success.....confirmed $signUpResult")
                }
            }

            override fun onFailure(exception: Exception?) {
                Log.e(TAG, "sign up failure: ${exception!!.localizedMessage}")
            }
        }

        btn_signup.setOnClickListener {
            val username = txt_username.text.toString()
            val email = txt_email.text.toString()
            val password = txt_pass.text.toString()

            //userattribute is the extra attribute required except username and password
            // (if there were other attributes like phone number or address, we would have to add attributes like email)
            userAttributes.addAttribute("email", email)

            val cognitoSettings = CognitoSetting(this)
            cognitoSettings.getUserPool().signUpInBackground(username, password, userAttributes, null, signupCallback)
        }
    }

    companion object{
        private val TAG = "MainActivity"
    }
}
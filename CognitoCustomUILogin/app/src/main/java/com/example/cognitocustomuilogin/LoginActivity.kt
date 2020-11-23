package com.example.cognitocustomuilogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val txt_user:EditText = findViewById(R.id.edt_login_user)
        val txt_pass: EditText = findViewById(R.id.edt_log_pass)
        val btn_log:Button = findViewById(R.id.btn_log_in)
        val txt_register: TextView = findViewById(R.id.txt_register_activity)

        val authenticationHandler: AuthenticationHandler = object : AuthenticationHandler{
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                Log.d(TAG, "Login Successful")
                UserDetails.username = userSession!!.username
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                startActivity(intent)
            }

            override fun getAuthenticationDetails(
                authenticationContinuation: AuthenticationContinuation?,
                userId: String?
            ) {
                Log.d(TAG, "in getAuthenticationDetails()...")

                //need to get the userid and password to continue
                val authenticationDetails = AuthenticationDetails(userId, txt_pass.text.toString(), null)

                //Pass the user sign-in credentials to the continuation
                authenticationContinuation?.setAuthenticationDetails(authenticationDetails)

                //allow the sign-in to continue
                authenticationContinuation!!.continueTask()
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                Log.d(TAG, "In getMFACode()...")
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                Log.d(TAG, "In authenticationChallenge()...")
            }

            override fun onFailure(exception: Exception?) {
                Log.d(TAG, "Login Failed: ${exception!!.localizedMessage}")
            }
        }

        btn_log.setOnClickListener {
            val cognitoSetting = CognitoSetting(this)
            val thisUser = cognitoSetting.getUserPool().getUser(txt_user.text.toString())

            //Sign-in the user
            Log.d(TAG, "In button clicked...")

//            thisUser.getSessionInBackground(authenticationHandler)
            GlobalScope.launch(Dispatchers.Default){
                thisUser.getSession(authenticationHandler)
            }
        }

        txt_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    companion object{
        private val TAG = "LoginActivity"
    }
}
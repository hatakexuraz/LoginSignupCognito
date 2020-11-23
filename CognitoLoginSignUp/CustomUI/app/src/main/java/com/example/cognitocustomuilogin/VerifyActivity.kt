package com.example.cognitocustomuilogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ViewAnimator
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class VerifyActivity : AppCompatActivity() {

    private lateinit var btn_go_log : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        val txt_code: EditText = findViewById(R.id.edt_verify_code)
        val txt_user_name : EditText = findViewById(R.id.edt_username)
        val btn_verify : Button = findViewById(R.id.btn_verify)
        btn_go_log = findViewById(R.id.btn_goto_login)

        btn_verify.setOnClickListener {
            //Started a coroutine to do the network based task in the background
            GlobalScope.launch(Dispatchers.Default) {

                //Callback handler for confirmSignUp API
                val confirmationCallBack = object : GenericHandler{
                    override fun onSuccess() {
                        //User was successfully confirmed
                        result = "Succeeded!"
                    }

                    override fun onFailure(exception: Exception?) {
                        //User confirmation failed
                        result = "Failed: ${exception!!.message}"
                    }
                }

                val cognitoSetting = CognitoSetting(this@VerifyActivity)
                val thisUser = cognitoSetting.getUserPool().getUser(txt_user_name.text.toString())

                //This will cause confirmation to fail if the user attribute (alias) is not available/ already created
                thisUser.confirmSignUp(txt_code.text.toString(), false, confirmationCallBack)

                withContext(Dispatchers.Main){
                    Log.d(TAG, "Confirmation result: ${result}")
                    if (result.equals("Succeeded!")){
                        txt_user_name.setText("")
                        txt_code.setText("")
                        Toast.makeText(this@VerifyActivity, "Your account have been verified. You can now log in.", Toast.LENGTH_SHORT).show()
                        btn_go_log.visibility = View.VISIBLE
                    }
                    else{
                        Toast.makeText(this@VerifyActivity, "${result}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btn_go_log.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    companion object{
        private val TAG = "VerifyActivity"
        private lateinit var cognitoPool : CognitoUserPool

        var result = ""
    }
}
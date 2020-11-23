package com.example.cognitocustomuilogin

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Regions

class CognitoSetting {

    private val userPoolId: String = "us-east-2_8sjgnxunj"
    private val clientID: String = "3vk0al6b9kb01oic0jrpr3t0a3"
    private val clientSecret: String = "i3r2hfbb53s4qi8j4cg5dvivgp48epgsv9qt3au42hvco6s6rvi"
    private val cognitoRegion: Regions = Regions.US_EAST_2
    var context : Context

    constructor(context: Context){
        this.context = context
    }

    //the entry point for all interactions with the user pool from your application
    fun getUserPool() : CognitoUserPool{
        return CognitoUserPool(context, userPoolId, clientID, clientSecret, cognitoRegion)
    }
}
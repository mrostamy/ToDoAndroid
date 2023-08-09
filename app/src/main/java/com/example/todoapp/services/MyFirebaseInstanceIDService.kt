package com.example.todoapp.services

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseInstanceIDService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("#######TOKEN:", token)
        Toast.makeText(applicationContext, "Token:$token", Toast.LENGTH_SHORT).show()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Toast.makeText(applicationContext, "onMessageReceived", Toast.LENGTH_SHORT).show()
    }
}
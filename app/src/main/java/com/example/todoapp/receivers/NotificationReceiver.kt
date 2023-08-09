package com.example.todoapp.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Toast.makeText(context, "RECIEVER RECIEVED!!!!!", Toast.LENGTH_SHORT).show()

        val message = intent?.extras?.getString("message")
        val title = intent?.extras?.getString("title")

        val notification = NotificationCompat.Builder(context!!, "chanelId")
            .setSmallIcon(android.R.drawable.alert_light_frame)
            .setContentText(message)
            .setContentTitle(title)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(12, notification)

    }
}
package com.example.todoapp.java_kotlin_convert

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class Convert2 {
    fun convert(context: Context?) {
        val intent = Intent()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
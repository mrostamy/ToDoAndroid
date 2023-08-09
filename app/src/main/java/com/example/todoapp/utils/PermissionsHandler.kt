package com.example.todoapp.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsHandler {

    fun getPermission(activity: Activity, permissions: Array<String>, requestCode: Int) {

        ActivityCompat.requestPermissions(activity, permissions, requestCode)

    }

    fun checkPermission(context: Context, permission: String): Boolean {

        val res = ContextCompat.checkSelfPermission(context, permission)
        if (res == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        return false

    }

}
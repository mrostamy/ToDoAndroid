package com.example.todoapp.utils

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.widget.Toast
import java.io.File

object Utils {

    fun makeMessage(context: Context, message: String, tag: String) {
        Log.i("$$$$$ $tag:", message)
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun convertImageUriToFile(activity: Activity, uri: Uri): File? {
        var cursor: Cursor? = null

        try {
            val proj = arrayOf(
                Media.DATA,
                Media._ID,
                MediaStore.Images.ImageColumns.ORIENTATION
            )
            cursor = activity.managedQuery(uri, proj, null, null, null)
            val fileColumnIndex =
                cursor.getColumnIndexOrThrow(Media.DATA)
            val orientationColumnIndex: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION)

            if (cursor.moveToFirst()) {
                var orientation: String = cursor.getString(orientationColumnIndex)
                return File(cursor.getString(fileColumnIndex))

            }
            return null
        } finally {
            cursor?.close()
        }
    }
}
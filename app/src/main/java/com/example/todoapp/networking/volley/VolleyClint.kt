package com.example.todoapp.networking.volley

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class VolleyClint(context: Context) {

    var context: Context

    init {
        this.context = context
    }


    fun stringRequest(url: String) {

        val respListener: Response.Listener<String> = Response.Listener<String> {

            Toast.makeText(context, it, Toast.LENGTH_LONG).show()

        }

        val errorListener: Response.ErrorListener = Response.ErrorListener {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }

        val request: StringRequest =
            StringRequest(Request.Method.GET, url, respListener, errorListener)

        val queue: RequestQueue = Volley.newRequestQueue(context)

        queue.add(request)


    }

    fun jsonObjectRequest(url: String) {

        val respListener: Response.Listener<JSONObject> = Response.Listener<JSONObject> {

            Toast.makeText(context, it.getString("name"), Toast.LENGTH_LONG).show()

        }

        val errorListener: Response.ErrorListener = Response.ErrorListener {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }

        val request: JsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, respListener, errorListener)

        val queue: RequestQueue = Volley.newRequestQueue(context)

        queue.add(request)


    }

    fun jsonArrayRequest(url: String) {

        val respListener: Response.Listener<JSONArray> = Response.Listener<JSONArray> {

            Toast.makeText(context, it.getJSONObject(0).getString("name"), Toast.LENGTH_LONG).show()

        }

        val errorListener: Response.ErrorListener = Response.ErrorListener {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }

        val request: JsonArrayRequest =
            JsonArrayRequest(Request.Method.GET, url, null, respListener, errorListener)

        val queue: RequestQueue = Volley.newRequestQueue(context)

        queue.add(request)


    }

    fun ImageRequest(url: String) {

        val respListener: Response.Listener<Bitmap> = Response.Listener<Bitmap> {

            Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()

        }

        val errorListener: Response.ErrorListener = Response.ErrorListener {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }

        val request: ImageRequest =
            ImageRequest(
                url,
                respListener,
                840,
                480,
                ImageView.ScaleType.CENTER,
                Bitmap.Config.RGB_565,
                errorListener
            )

        val queue: RequestQueue = Volley.newRequestQueue(context)

        queue.add(request)


    }

    fun MultipartRequest(url: String) {


    }


}
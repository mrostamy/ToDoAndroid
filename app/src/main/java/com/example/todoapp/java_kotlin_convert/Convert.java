package com.example.todoapp.java_kotlin_convert;

import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.todoapp.networking.retrofit.RetrofitClient;
import com.example.todoapp.networking.retrofit.User;
import com.example.todoapp.networking.retrofit.model.User1;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Convert {

    BottomNavigationView navigationView;

    public void convert() {

        navigationView.setOnItemSelectedListener(new Listener());


    }


    static class Listener implements NavigationBarView.OnItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            return false;
        }
    }
}

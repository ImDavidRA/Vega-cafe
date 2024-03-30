package com.example.food2.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {

    public String TAG = "Vega-Café";
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

}
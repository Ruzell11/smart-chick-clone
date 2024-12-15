package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class AdminFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_fragment);

        String username = getIntent().getStringExtra("username");
// Display the username in a TextView
        TextView usernameTextView = findViewById(R.id.usernameTextView); // Your TextView in the layout
        usernameTextView.setText("Welcome, " + username);

        ImageView adminDashboard = findViewById(R.id.adminDashboard);
        adminDashboard.setOnClickListener(view ->{
            Intent intent = new Intent(this, AdminDashboard.class);
            startActivity(intent);
        });

    }
}
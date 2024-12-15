package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FarmerFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.farmer_fragment);
        String username = getIntent().getStringExtra("username");
// Display the username in a TextView
        TextView usernameTextView = findViewById(R.id.usernameTextView); // Your TextView in the layout
        usernameTextView.setText("Welcome, " + username);

        ImageView farmerDashboard = findViewById(R.id.farmerDashboard);
        farmerDashboard.setOnClickListener(view ->{
            Intent intent = new Intent(this, FarmerDashboardActivity.class);
            startActivity(intent);
        });

    }
}
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboard extends AppCompatActivity {

    CardView editUserInfo, chickenInfo, qrCode, temperature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_dashboard);

        editUserInfo = findViewById(R.id.editUserInfo);
        chickenInfo = findViewById(R.id.chickenInfo);
        qrCode = findViewById(R.id.qrCode);
        temperature = findViewById(R.id.temperature);

        String username = getIntent().getStringExtra("username");

        ImageView menuIcon = findViewById(R.id.menu);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AdminDashboard.this, v);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // Display a Toast message
                        Toast.makeText(AdminDashboard.this, "You've been logged out", Toast.LENGTH_SHORT).show();


                        if(item.getItemId() == R.id.logout){
                            startActivity(new Intent(AdminDashboard.this, LoginActivity.class));
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });


        editUserInfo.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditUserInformationActivity.class);
            startActivity(intent);
        });


        chickenInfo.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChickenInformationActivity.class);
            startActivity(intent);
        });

        qrCode.setOnClickListener(view -> {
            Intent intent = new Intent(this, QrScannerActivity.class);
            startActivity(intent);
        });


    }
}


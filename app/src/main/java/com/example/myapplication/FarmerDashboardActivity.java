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

public class FarmerDashboardActivity extends AppCompatActivity {

    CardView chickenInfo, qrCode, temperature;
    ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_dashboard);

        chickenInfo = findViewById(R.id.chickenInfo);
        qrCode = findViewById(R.id.qrCode);
        temperature = findViewById(R.id.temperature);
        menuIcon = findViewById(R.id.menu);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(FarmerDashboardActivity.this, v);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // Display a Toast message
                        Toast.makeText(FarmerDashboardActivity.this, "You've been logged out", Toast.LENGTH_SHORT).show();


                        if(item.getItemId() == R.id.logout){
                            startActivity(new Intent(FarmerDashboardActivity.this, LoginActivity.class));
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        chickenInfo.setOnClickListener(view ->{
            Intent intent = new Intent(this, ChickenInformationActivity.class);
            startActivity(intent);
        });

        qrCode.setOnClickListener(view ->{
            Intent intent = new Intent(this, QrScannerActivity.class);
            startActivity(intent);
        });

    }
}
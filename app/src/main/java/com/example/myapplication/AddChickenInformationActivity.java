package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ModelClass.Chicken;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddChickenInformationActivity extends AppCompatActivity {

    private EditText chickenBreedEditText, chickenAgeEditText, chickenBirthdateEditText, chickenVitaminsEditText;
    private Button addChickenInformationBtn;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_chicken_information);

        // Initialize UI elements
        chickenBreedEditText = findViewById(R.id.chickenBreedEditText);
        chickenAgeEditText = findViewById(R.id.chickenAgeEditText);
        chickenBirthdateEditText = findViewById(R.id.chickenBirthdateEditText);
        chickenVitaminsEditText = findViewById(R.id.chickenVitaminsEditText);
        addChickenInformationBtn = findViewById(R.id.addChickenInformationBtn);

        ImageView homeBtn = findViewById(R.id.homeBtn);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();

        addChickenInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input data
                String breed = chickenBreedEditText.getText().toString().trim();
                String age = chickenAgeEditText.getText().toString().trim();
                String birthdate = chickenBirthdateEditText.getText().toString().trim();
                String vitamins = chickenVitaminsEditText.getText().toString().trim();

                // Check if any fields are empty
                if (breed.isEmpty() || age.isEmpty() || birthdate.isEmpty() || vitamins.isEmpty()) {
                    Toast.makeText(AddChickenInformationActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the chicken information to Firebase
                DatabaseReference chickenRef = database.getReference("chickens").push();
                Chicken chicken = new Chicken(breed, age, birthdate, vitamins);
                chickenRef.setValue(chicken)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddChickenInformationActivity.this, "Chicken added successfully!", Toast.LENGTH_SHORT).show();

                                // Redirect back to ChickenInformationActivity
                                Intent intent = new Intent(AddChickenInformationActivity.this, ChickenInformationActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AddChickenInformationActivity.this, "Failed to add chicken information", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("FirebaseError", "Error adding chicken", e);
                            Toast.makeText(AddChickenInformationActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}

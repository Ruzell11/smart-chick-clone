package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ModelClass.Chicken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class ChickenInformationActivity extends AppCompatActivity {

    private ListView chickenListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> chickenList;
    private ArrayList<String> chickenKeys;
    private DatabaseReference chickensRef;

    private EditText chickenBreedEditText, chickenAgeEditText, chickenBirthdateEditText, chickenVitaminsEditText;
    private Button updateBtn, deleteBtn, generateQrCodeBtn;

    private String selectedChickenKey = null;

    ImageView addChickenInformation;
    ImageView qrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chicken_information);

        chickenListView = findViewById(R.id.chickenListView);
        addChickenInformation = findViewById(R.id.addChickenInformation);
        chickenBreedEditText = findViewById(R.id.chickenBreedEditText);
        chickenAgeEditText = findViewById(R.id.chickenAgeEditText);
        chickenBirthdateEditText = findViewById(R.id.chickenBirthdateEditText);
        chickenVitaminsEditText = findViewById(R.id.chickenVitaminsEditText);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        generateQrCodeBtn = findViewById(R.id.generateQrCodeBtn);
        qrCodeImageView = findViewById(R.id.qrcodeImageView);

        ImageView homeBtn = findViewById(R.id.homeBtn);

        chickenList = new ArrayList<>();
        chickenKeys = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chickenList);
        chickenListView.setAdapter(adapter);

        chickensRef = FirebaseDatabase.getInstance().getReference("chickens");

        loadChickenData();

        addChickenInformation.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddChickenInformationActivity.class);
            startActivity(intent);
        });

        chickenListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedChickenKey = chickenKeys.get(position);
            chickensRef.child(selectedChickenKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Chicken chicken = snapshot.getValue(Chicken.class);
                    if (chicken != null) {
                        chickenBreedEditText.setText(chicken.getBreed());
                        chickenAgeEditText.setText(chicken.getAge());
                        chickenBirthdateEditText.setText(chicken.getBirthdate());
                        chickenVitaminsEditText.setText(chicken.getVitamins());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(ChickenInformationActivity.this, "Failed to fetch chicken data.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        generateQrCodeBtn.setOnClickListener(view -> generateQRCode());

        updateBtn.setOnClickListener(view -> {
            if (selectedChickenKey != null) {
                String updatedBreed = chickenBreedEditText.getText().toString();
                String updatedAge = chickenAgeEditText.getText().toString();
                String updatedBirthdate = chickenBirthdateEditText.getText().toString();
                String updatedVitamins = chickenVitaminsEditText.getText().toString();

                Chicken updatedChicken = new Chicken(updatedBreed, updatedAge, updatedBirthdate, updatedVitamins);
                chickensRef.child(selectedChickenKey).setValue(updatedChicken).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Chicken updated successfully!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(this, "Failed to update chicken.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please select a chicken to update.", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(view -> {
            if (selectedChickenKey != null) {
                chickensRef.child(selectedChickenKey).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Chicken deleted successfully!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(this, "Failed to delete chicken.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please select a chicken to delete.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadChickenData() {
        chickensRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chickenList.clear();
                chickenKeys.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chicken chicken = snapshot.getValue(Chicken.class);
                    if (chicken != null) {
                        String chickenInfo = "Breed: " + chicken.getBreed() +
                                "\nAge: " + chicken.getAge() +
                                "\nBirthdate: " + chicken.getBirthdate() +
                                "\nVitamins: " + chicken.getVitamins();
                        chickenList.add(chickenInfo);
                        chickenKeys.add(snapshot.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChickenInformationActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateQRCode() {
        if (selectedChickenKey != null && !selectedChickenKey.isEmpty()) {
            String breed = chickenBreedEditText.getText().toString();
            String age = chickenAgeEditText.getText().toString();
            String birthdate = chickenBirthdateEditText.getText().toString();
            String vitamins = chickenVitaminsEditText.getText().toString();

            String qrContent = "Breed: " + breed + "\n"
                    + "Age: " + age + "\n"
                    + "Birthdate: " + birthdate + "\n"
                    + "Vitamins: " + vitamins;

            Intent intent = new Intent(this, QrDisplay.class);
            intent.putExtra("chickenDetails", qrContent);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please select a chicken to generate QR code.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        chickenBreedEditText.setText("");
        chickenAgeEditText.setText("");
        chickenBirthdateEditText.setText("");
        chickenVitaminsEditText.setText("");
        selectedChickenKey = null;
    }
}

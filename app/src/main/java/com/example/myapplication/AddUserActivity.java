package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        EditText usernameField = findViewById(R.id.username);
        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);
        Spinner roleSpinner = findViewById(R.id.roleSpinner);
        Button addButton = findViewById(R.id.addBtn);

        // Populate role spinner
        String[] roles = {"Select your roles", "Farmer", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Add user button click listener
        addButton.setOnClickListener(view -> {
            String username = usernameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String role = roleSpinner.getSelectedItem().toString();

            // Check if other fields are empty
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(AddUserActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate password length
            if (password.length() < 6) {
                Toast.makeText(AddUserActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if role is selected
            if (role.equals("Select your roles")) {
                Toast.makeText(AddUserActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
                return; // Prevent proceeding further if role is not selected
            }


            // Create user with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Save user details to Firebase Realtime Database
                    String uid = auth.getCurrentUser().getUid();
                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    user.put("email", email);
                    user.put("role", role);

                    database.getReference("users").child(uid).setValue(user).addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            Intent intent = new Intent(this, EditUserInformationActivity.class);
                            startActivity(intent);
                            Toast.makeText(AddUserActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Go back to the previous screen
                        } else {
                            Toast.makeText(AddUserActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                    Toast.makeText(AddUserActivity.this, "Failed to create user: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

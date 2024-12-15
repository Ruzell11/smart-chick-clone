package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginBtn);

        loginButton.setOnClickListener(view -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // For admin login using hardcoded credentials
            if (email.equals("admin") && password.equals("admin123")) {
                Intent intent = new Intent(this, AdminFragmentActivity.class);
                startActivity(intent);
                return;
            }

            // For user login with Firebase authentication
            if (!email.isEmpty() && !password.isEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login successful, fetch user data (role) from Firebase
                        FirebaseUser currentUser = auth.getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid(); // Get user ID
                            fetchUserRoleAndUsernameAndNavigate(userId); // Pass userId to fetch role and username
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserRoleAndUsernameAndNavigate(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String role = dataSnapshot.child("role").getValue(String.class);  // Fetch role
                    String username = dataSnapshot.child("username").getValue(String.class);  // Fetch username

                    // Navigate to respective dashboards based on role
                    if ("Admin".equals(role)) {
                        // If the user is an admin, navigate to Admin Dashboard and pass username
                        Intent intent = new Intent(LoginActivity.this, AdminFragmentActivity.class);
                        intent.putExtra("username", username);  // Pass the username
                        startActivity(intent);
                    } else if ("Farmer".equals(role)) {
                        // If the user is a farmer, navigate to Farmer Dashboard and pass username
                        Intent intent = new Intent(LoginActivity.this, FarmerFragmentActivity.class);
                        intent.putExtra("username", username);  // Pass the username
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid role", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

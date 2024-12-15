package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ArrayAdapter.ArrayAdapter;
import com.example.myapplication.ModelClass.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditUserInformationActivity extends AppCompatActivity {

    private ListView userListView;
    private ArrayAdapter userArrayAdapter;
    private List<User> userList;
    private DatabaseReference databaseReference;
    private EditText usernameEditText, emailEditText, roleEditText;
    private Button updateBtn, deleteBtn;
    private User selectedUser;
    private FirebaseAuth auth;
    ImageView add, logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_information);

        // Initialize UI components
        userListView = findViewById(R.id.userListView);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        roleEditText = findViewById(R.id.roleEditText);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        add = findViewById(R.id.add);
        logo = findViewById(R.id.logo);

        add.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddUserActivity.class);
            startActivity(intent);
        });

        // Initialize list and adapter
        userList = new ArrayList<>();
        userArrayAdapter = new ArrayAdapter(this, userList);
        userListView.setAdapter(userArrayAdapter);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();

        // Fetch users from Firebase
        fetchUsersFromDatabase();

        // Set item click listener to populate the fields with user data
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedUser = userList.get(position);
            // Fill EditText fields with selected user data
            usernameEditText.setText(selectedUser.getUsername());
            emailEditText.setText(selectedUser.getEmail());
            roleEditText.setText(selectedUser.getRole());
        });

        // Set click listener for the update button
        updateBtn.setOnClickListener(v -> {
            if (selectedUser != null) {
                String updatedUsername = usernameEditText.getText().toString();
                String updatedEmail = emailEditText.getText().toString();
                String updatedRole = roleEditText.getText().toString();

                // Check if the fields are valid
                if (!updatedUsername.isEmpty() && !updatedEmail.isEmpty() && !updatedRole.isEmpty()) {
                    // Update user email in Firebase Authentication and Realtime Database
                    updateUserEmail(selectedUser.getId(), updatedUsername, updatedEmail, updatedRole);
                } else {
                    Toast.makeText(EditUserInformationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EditUserInformationActivity.this, "Select a user to update", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for the delete button
        deleteBtn.setOnClickListener(v -> {
            if (selectedUser != null) {
                // Delete user from Firebase
                deleteUserFromDatabase(selectedUser.getId());
            } else {
                Toast.makeText(EditUserInformationActivity.this, "Select a user to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch users from Firebase and update the ListView
    private void fetchUsersFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();  // Clear the existing list to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        user.setId(snapshot.getKey());  // Set the Firebase key as the user ID
                        userList.add(user);
                    }
                }
                userArrayAdapter.notifyDataSetChanged();  // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditUserInformationActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserEmail(String userId, String username, String oldEmail, String newEmail, String password) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Re-authenticate the user using the old email and the user's password
            AuthCredential credential = EmailAuthProvider.getCredential(oldEmail, password);
            currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Update email in Firebase Authentication
                    currentUser.updateEmail(newEmail).addOnCompleteListener(emailUpdateTask -> {
                        if (emailUpdateTask.isSuccessful()) {
                            // If email update in Firebase Authentication is successful, update the email in Firebase Realtime Database
                            updateEmailInDatabase(userId, newEmail);
                        } else {
                            Toast.makeText(EditUserInformationActivity.this, "Failed to update email in Authentication: " + emailUpdateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(EditUserInformationActivity.this, "Re-authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(EditUserInformationActivity.this, "No user signed in", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmailInDatabase(String userId, String newEmail) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.child("email").setValue(newEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditUserInformationActivity.this, "Email updated successfully!", Toast.LENGTH_SHORT).show();
                // Manually refresh the data on the phone after the update
                fetchUsersFromDatabase();  // Ensure the latest data is fetched from Firebase
            } else {
                Toast.makeText(EditUserInformationActivity.this, "Failed to update email in Database.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUserEmail(String userId, String username, String email, String role) {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // Update email in Firebase Authentication
            currentUser.updateEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Update user info in Realtime Database
                    updateUserInDatabase(userId, username, email, role);
                } else {
                    Toast.makeText(EditUserInformationActivity.this, "Failed to update email in Authentication: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateUserInDatabase(String userId, String username, String email, String role) {
        DatabaseReference userRef = databaseReference.child(userId);
        userRef.child("username").setValue(username);
        userRef.child("email").setValue(email);
        userRef.child("role").setValue(role).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditUserInformationActivity.this, "User updated successfully!", Toast.LENGTH_SHORT).show();
                // Manually refresh the data on the phone after the update
                fetchUsersFromDatabase();  // Ensure the latest data is fetched from Firebase
            } else {
                Toast.makeText(EditUserInformationActivity.this, "Failed to update user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Delete user from Firebase
    private void deleteUserFromDatabase(String userId) {
        DatabaseReference userRef = databaseReference.child(userId);
        userRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditUserInformationActivity.this, "User deleted successfully!", Toast.LENGTH_SHORT).show();
                // Refresh the ListView after deletion
                fetchUsersFromDatabase();
            } else {
                Toast.makeText(EditUserInformationActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

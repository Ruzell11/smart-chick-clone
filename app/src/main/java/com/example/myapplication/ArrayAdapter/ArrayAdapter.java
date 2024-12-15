package com.example.myapplication.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.ModelClass.User;
import com.example.myapplication.R;

import java.util.List;

// Custom adapter for displaying user data
public class ArrayAdapter extends android.widget.ArrayAdapter<User> {

    // Constructor for the adapter
    public ArrayAdapter(Context context, List<User> users) {
        super(context, 0, users);  // 0 is the resource ID, not used here as we're inflating a custom layout
    }

    // Overriding the getView method to provide custom layout for each item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);

        // Check if the existing view is being reused; otherwise, inflate a new view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);
        }

        // Find the views in the item layout
        TextView usernameText = convertView.findViewById(R.id.usernameText);
        TextView emailText = convertView.findViewById(R.id.emailText);
        TextView roleText = convertView.findViewById(R.id.roleText);

        // Set the data to the respective views
        if (user != null) {
            usernameText.setText("Username: " + user.getUsername());
            emailText.setText("Email: " + user.getEmail());
            roleText.setText("Role: " + user.getRole());
        }

        // Return the completed view
        return convertView;
    }
}

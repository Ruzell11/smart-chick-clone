package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class QrDisplay extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private TextView chickenDetailsTextView;
    private Button downloadQRButton;
    private Bitmap qrBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_display);

        qrCodeImageView = findViewById(R.id.qrcodeImageView);
        chickenDetailsTextView = findViewById(R.id.chickenDetailsTextView);
        downloadQRButton = findViewById(R.id.downloadQR);

        ImageView homeBtn = findViewById(R.id.homeBtn);


        homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminDashboard.class);
            startActivity(intent);
        });

        // Get the chicken details
        String chickenDetails = getIntent().getStringExtra("chickenDetails");

        if (chickenDetails != null) {
            chickenDetailsTextView.setText(chickenDetails);

            try {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(chickenDetails, BarcodeFormat.QR_CODE, 512, 512);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                qrBitmap = barcodeEncoder.createBitmap(bitMatrix);

                qrCodeImageView.setImageBitmap(qrBitmap);
            } catch (WriterException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to generate QR Code", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No chicken details received.", Toast.LENGTH_SHORT).show();
        }

        // Set up the download button
        downloadQRButton.setOnClickListener(view -> {
            // Call the downloadQR method
            downloadQR();

            // Navigate to SecondActivity
            Intent intent = new Intent(QrDisplay.this, QrScannerActivity.class);
            startActivity(intent);
        });
    }


    public void downloadQR() {
        if (qrBitmap == null) {
            Toast.makeText(this, "QR Code is not available to download", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Define the file name
            String fileName = "chicken_qr_code.png";

            // Create content values to specify metadata for the file
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName); // File name
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png"); // Mime type
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QR Codes"); // Save in "Pictures/QR Codes"

            // Use MediaStore to insert the content and get a URI for the new image
            Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            // Open an output stream to write the bitmap to the URI
            try (OutputStream outputStream = getContentResolver().openOutputStream(imageUri)) {
                if (outputStream != null) {
                    qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // Compress and save the bitmap
                    Toast.makeText(this, "QR Code saved to: " + imageUri, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Failed to save QR Code", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save QR Code: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    }




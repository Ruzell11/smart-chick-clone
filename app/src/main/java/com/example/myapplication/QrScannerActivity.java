package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.helperClass.Helper;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeReader;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Environment;

import java.io.IOException;

public class QrScannerActivity extends AppCompatActivity {

    private TextView informationTextView;
    private ImageView scanQrImageView;
    private ImageView selectImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);

        informationTextView = findViewById(R.id.information);
        scanQrImageView = findViewById(R.id.imageView2);
        selectImageView = findViewById(R.id.imageView10);

        ImageView homeBtn = findViewById(R.id.homeBtn);

        // Set click listeners
        scanQrImageView.setOnClickListener(v -> startQRScan());
        selectImageView.setOnClickListener(v -> selectFile());
    }

    public void downloadFile(String fileUrl, String fileName) {
        try {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(fileUrl);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("Downloading File");
            request.setDescription("Downloading " + fileName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Save file to the "Downloads" directory
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            // Enqueue the download
            if (downloadManager != null) {
                downloadManager.enqueue(request);
                Toast.makeText(this, "Download started...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Download Manager not available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Download failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Start QR code scanning
    private void startQRScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan a QR Code");
        integrator.setCameraId(0); // Use the back camera
        integrator.initiateScan();
    }

    // Handle QR code scan result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Display scanned QR code content
                informationTextView.setText(result.getContents());
            } else {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Open file picker for selecting an image from Files app
    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Restrict selection to image files
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        selectFileLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> selectFileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedFileUri = result.getData().getData();
                    if (selectedFileUri != null) {
                        decodeQRCodeFromFile(selectedFileUri);
                    }
                } else {
                    Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
                }
            });

    // Decode QR code from the selected file
    private void decodeQRCodeFromFile(Uri fileUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);

            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new Helper(bitmap)));
            Reader reader = new QRCodeReader();
            Result result = reader.decode(binaryBitmap);

            informationTextView.setText(result.getText());
        } catch (IOException e) {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "No QR code found in the file", Toast.LENGTH_SHORT).show();
        }
    }
}

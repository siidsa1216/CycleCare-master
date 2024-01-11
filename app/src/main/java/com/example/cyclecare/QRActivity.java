package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cyclecare.databinding.ActivityQractivityBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


public class QRActivity extends AppCompatActivity {

    ActivityQractivityBinding binding;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    String bikeId, bikeName, profileId, cycleCareId, parkId;
    String retrievedBikeId;

    @Override
    public void onBackPressed() {
        // Uncomment the line below if you want to allow the back press
        // super.onBackPressed();
        super.onBackPressed();
        showExitConfirmationDialog();
        // Leave this method empty to disable the back press
    }

    private void showExitConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(QRActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to quit parking?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Delete the saved park details
            deleteSavedParkDetails();
            finish(); // Close the activity
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing, just close the dialog
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteSavedParkDetails() {
        // Retrieve the parkId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String parkId = sharedPreferences.getString("parkId", "");

        if (parkId != null && !parkId.isEmpty()) {
            // Delete the park details from the database or perform any necessary cleanup
            DatabaseReference parkRef = FirebaseDatabase.getInstance().getReference("Park");
            parkRef.child(firebaseUser.getUid()).child(parkId).removeValue();

            // Clear the parkId from SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("parkId");
            editor.apply();

            Toast.makeText(this, "Parking cancelled.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQractivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        firebaseDatabase = FirebaseDatabase.getInstance();  // Initialize FirebaseDatabase
        firebaseStorage = FirebaseStorage.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        retrievedBikeId = sharedPreferences.getString("bikeId", "");

        // Check if retrievedBikeId is not null or empty before using it
        if (retrievedBikeId != null && !retrievedBikeId.isEmpty()) {
            // BikeId is available, you can use it here
            // Example: display it in a TextView
        } else {
            // Handle the case where retrievedBikeId is null or empty
            Toast.makeText(this, "Bike ID not found", Toast.LENGTH_SHORT).show();
        }


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            profileId = firebaseUser.getUid();
            // Rest of your code using userId
        } else {
            // Handle the case where the user is not signed in
            // You might want to redirect the user to the sign-in screen or handle it appropriately.
        }

        Intent intent = getIntent();
        if (intent != null) {
            bikeId = intent.getStringExtra("bikeId");
            bikeName = intent.getStringExtra("bikeName");
            cycleCareId = intent.getStringExtra("cycleCareId");
        }

        binding.generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkYourBike();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            startActivity(new Intent(QRActivity.this, SelectCyccleCareActivity.class));
            }
        });

    }

    private void showSuccessDialog(Bitmap qrCodeBitmap) {
        ConstraintLayout successConstraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(QRActivity.this).inflate(R.layout.success_dialog, successConstraintLayout);

        // Use the inflated view to find the button
        Button successDone = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(QRActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        successDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(QRActivity.this, ScanActivity.class);
                intent.putExtra("bikeId", retrievedBikeId);
                intent.putExtra("parkId", parkId);
                intent.putExtra("cycleCareId", cycleCareId);
                startActivity(intent);
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void parkYourBike() {

        TimeZone timeZone = TimeZone.getTimeZone("Asia/Manila");

        long timestamp = System.currentTimeMillis();
        String dateTime = convertTimestampToDateTime(timestamp, timeZone);
        long timeEnd = addNineHoursToTimestamp(timestamp);
        String endTime = convertTimestampToDateTime(timeEnd, timeZone);
        boolean status = false;

        DatabaseReference parkRef = FirebaseDatabase.getInstance().getReference("Park");
        DatabaseReference QRRef = FirebaseDatabase.getInstance().getReference("QR");
        DatabaseReference BikeRef = FirebaseDatabase.getInstance().getReference("Bikes");
        parkId = parkRef.push().getKey(); // Save parkId globally

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("parkId", parkId);
        editor.apply();



        // Create a HashMap to store parking information
        HashMap<String, Object> parkMap = new HashMap<>();
        parkMap.put("parkId", parkId);
        parkMap.put("bikeId", retrievedBikeId);
        parkMap.put("cycleCareId", cycleCareId);
        parkMap.put("userId", profileId);
        parkMap.put("QRCodeURL", "");
        parkMap.put("timeEnd", endTime);
        parkMap.put("timestamp", dateTime);
        parkMap.put("status", status); //false if not ongoing


        parkRef.child(firebaseUser.getUid()).child(parkId).setValue(parkMap).addOnSuccessListener(aVoid -> {
            Bitmap qrCodeBitmap = generateQR(profileId);
            showSuccessDialog(qrCodeBitmap);
        }).addOnFailureListener(e -> {
            // Handle any errors that occurred during the save process
            // For example, show an error message
            Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show();
        });



        QRRef.child(cycleCareId).child("parkId").setValue(profileId);
        Log.d("UnLockActivity", "Received parkId: " + profileId);

        BikeRef.child(profileId).child(retrievedBikeId).child("isparked").setValue(true);
        Log.d("UnLockActivity", "Received bikeId: " + retrievedBikeId);
    }


    private String convertTimestampToDateTime(long timestamp, TimeZone timeZone) {
        // Use SimpleDateFormat to format the date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Set the time zone
        sdf.setTimeZone(timeZone);

        // Create a Date object from the timestamp
        Date date = new Date(timestamp);

        // Format the date as a string
        return sdf.format(date);
    }


    private long addNineHoursToTimestamp(long timestamp) {
        // Create a Calendar object and set its time to the timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        // Add nine hours
        calendar.add(Calendar.HOUR_OF_DAY, 9);

        // Get the updated timestamp
        return calendar.getTimeInMillis();
    }

    private Bitmap generateQR(String userId) {
        // Adjust width and height as needed
        int width = 500;
        int height = 500;

        Bitmap qrCodeBitmap = QRcodeGenerator.generateQRCode(userId, width, height);

        if (qrCodeBitmap != null) {
            saveQRCodeToStorage(qrCodeBitmap);
        }

        return qrCodeBitmap;
    }

    private void saveQRCodeToStorage(Bitmap qrCodeBitmap) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("qr_codes/" + parkId + "/qr_code_image.png");

        // Convert bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                String url = downloadUri.toString();

                DatabaseReference parkRef = firebaseDatabase.getInstance().getReference("Park");
                parkRef.child(firebaseUser.getUid()).child(parkId).child("QRCodeURL").setValue(url);
            } else {
                // Handle errors here
                Exception e = task.getException();
                e.printStackTrace();
            }
        });
    }
}

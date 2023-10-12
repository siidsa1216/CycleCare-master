package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import java.util.HashMap;

public class QRActivity extends AppCompatActivity {

    ActivityQractivityBinding binding;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    String bikeId, bikeName, profileId, cycleCareId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQractivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseDatabase = FirebaseDatabase.getInstance();  // Initialize FirebaseDatabase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


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
            cycleCareId = intent.getStringExtra("cyclecareId");
        }

        binding.generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkYourBike();
            }
        });



    }

    private void parkYourBike() {
        // Create a timestamp
        long timestamp = System.currentTimeMillis();
        String dateTime = TimeStamp.convertTimestampToDateTime(timestamp);
        long timeEnd = addNineHoursToTimestamp(timestamp);
        String endTime = TimeStamp.convertTimestampToDateTime(timeEnd);

        // Display or save the readable date and tim
//        Log.d("Timestamp", "Readable Date and Time: " + dateTime);
//        Log.d("Timestamp", "End Time (after adding 9 hours): " + endTime);
        DatabaseReference parkRef = firebaseDatabase.getInstance().getReference("Park");
        // Generate a unique key for the new bike entry
        String parkId = parkRef.push().getKey();

        // Create a HashMap to store parking information
        HashMap<String, Object> parkMap = new HashMap<>();
        parkMap.put("parkId", parkId);
        parkMap.put("cyclecareId", cycleCareId);
        parkMap.put("bikeId", bikeId);
        parkMap.put("userId", profileId);
        parkMap.put("QRCodeURL", "");
        parkMap.put("timeEnd", endTime);
        parkMap.put("timestamp", dateTime);
        parkMap.put("status", "Unlocked");


        // Save the bike information to the database
        parkRef.child(parkId).setValue(parkMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred during the save process
                    // For example, show an error message
                });

        generateQR(parkId);
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


    private String generateQR(String parkId) {

        String data = "YourDataToBeEncoded"; // Replace with your data

        // Adjust width and height as needed
        int width = 500;
        int height = 500;

        Bitmap qrCodeBitmap = QRcodeGenerator.generateQRCode(data, width, height);

        if (qrCodeBitmap != null) {
            binding.displayQR.setImageBitmap(qrCodeBitmap);
            saveQRCodeToStorage(qrCodeBitmap, parkId);
        }

        return null;
    }

    private void saveQRCodeToStorage(Bitmap qrCodeBitmap, String parkId) {
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
                parkRef.child(parkId).child("QRCodeURL").setValue(url);
            } else {
                // Handle errors here
                Exception e = task.getException();
                e.printStackTrace();
            }
        });
    }

}
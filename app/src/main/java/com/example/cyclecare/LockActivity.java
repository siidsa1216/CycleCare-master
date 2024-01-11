package com.example.cyclecare;

import static com.example.cyclecare.TimeStamp.convertTimestampToDateTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyclecare.databinding.ActivityLockBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.TimeZone;

public class LockActivity extends AppCompatActivity {

    public static TextView status;
    ActivityLockBinding binding;
    FirebaseDatabase firebaseDatabase;
    String parkId, profileId, cycleCareId;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        // Retrieve bikeId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String receivedbikeId = sharedPreferences.getString("bikeId", "");

        Log.d("LockActivity", "ReceivedbikeId: " + receivedbikeId);


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
            receivedbikeId= intent.getStringExtra("bikeId");
            parkId = intent.getStringExtra("parkId");
            cycleCareId = intent.getStringExtra("cycleCareId");
        }

        Log.d("LockActivity", "Received bikeId: " + receivedbikeId);

        status = findViewById(R.id.status);

        binding.status.setText("UNLOCKED");
        binding.lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateLock();
            }
        });

    }

    private void activateLock() {
        DatabaseReference parkReference = FirebaseDatabase.getInstance().getReference("Park").child(firebaseUser.getUid()).child(parkId);

        parkReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String cycleCareId = dataSnapshot.child("cycleCareId").getValue(String.class);
                    if (cycleCareId != null) {

                        String retrievedCycleCareId = cycleCareId;
                        // Show dialog only if the state is updated successfully
                        updateStateStatus(retrievedCycleCareId);
                    } else {
                        // Handle the case where cycleCareId is null
                    }
                } else {
                    // Handle the case where the dataSnapshot doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


    }


    private void updateStateStatus(String retrievedCycleCareId) {
        boolean locked = true;

        DatabaseReference lockActuatorReference = FirebaseDatabase.getInstance().getReference("LockActuator").child(retrievedCycleCareId);
        lockActuatorReference.child("state").setValue(locked);

        DatabaseReference cyclecareReference = FirebaseDatabase.getInstance().getReference("CycleCareUnit").child(retrievedCycleCareId);
        cyclecareReference.child("isoccupied").setValue(locked);

        showLockDialog();
    }




    private void showLockDialog() {
        ConstraintLayout successConstraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(LockActivity.this).inflate(R.layout.lockdialog, successConstraintLayout);

// Use the inflated view to find the button
        Button successDone = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(LockActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        successDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                successDone.setEnabled(false);

                saveLockHistory();

                // Delay for 20 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        successDone.setEnabled(true);
                        startActivity(new Intent(LockActivity.this, MainActivity.class));
                        finish();
                    }
                }, 5000); // 10 seconds delay
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }

    private void saveLockHistory() {


        // Retrieve bikeId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String receivedbikeId = sharedPreferences.getString("bikeId", "");

        Log.d("LockActivity", "ReceivedbikeIdsddddsasasasa: " + receivedbikeId);


        TimeZone timeZone = TimeZone.getTimeZone("Asia/Manila");

        long timestamp = System.currentTimeMillis();
        String dateTime = convertTimestampToDateTime(timestamp, timeZone);


        DatabaseReference parkRef = FirebaseDatabase.getInstance().getReference("History");

        String historyId = parkRef.push().getKey(); // Save parkId globally

        HashMap<String, Object> parkMap = new HashMap<>();
        parkMap.put("parkId", parkId);
        parkMap.put("bikeId", receivedbikeId);
        parkMap.put("historyId", historyId);
        parkMap.put("event", "Bike locked.");
        parkMap.put("time", dateTime);


        parkRef.child(receivedbikeId).child(historyId).setValue(parkMap).addOnSuccessListener(aVoid -> {
            Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            // Handle any errors that occurred during the save process
            // For example, show an error message
            Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
        });
    }
}
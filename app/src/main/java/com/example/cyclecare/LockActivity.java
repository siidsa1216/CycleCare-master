package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyclecare.databinding.ActivityLockBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            parkId = intent.getStringExtra("parkId");
            cycleCareId = intent.getStringExtra("cycleCareId");
        }

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
        boolean locked = true;

        DatabaseReference databaseReference = firebaseDatabase.getReference("Park");

        databaseReference.child(firebaseUser.getUid()).child(parkId).child("status").setValue(locked)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Locked activated, wait for the locking mechanism to stop before leaving your bike.", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(this, ViewStatusActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred during the save process
                    // For example, show an error message
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
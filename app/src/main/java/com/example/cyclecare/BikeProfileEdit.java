package com.example.cyclecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cyclecare.Model.Bike;
import com.example.cyclecare.databinding.ActivityBikeProfileEditBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BikeProfileEdit extends AppCompatActivity {

    ActivityBikeProfileEditBinding binding;
    FirebaseUser firebaseUser;
    DatabaseReference bikeReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBikeProfileEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String bikeId = sharedPreferences.getString("bikeId", "");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        bikeReference = FirebaseDatabase.getInstance().getReference("Bikes")
                .child(firebaseUser.getUid());


        // Retrieve the data from the Intent
        Intent intent = getIntent();
        if (intent != null) {

            String bikeName = intent.getStringExtra("bikeName");
            String bikeBrand = intent.getStringExtra("bikeBrand");
            String bikeModel = intent.getStringExtra("bikeModel");



            // Now you can use the data in SecondActivity
            // For example, set it to TextViews in your layout

            binding.bikeNametxt.setText(bikeName);
            binding.bikeBrandtxt.setText(bikeBrand);
            binding.bikeModeltxt.setText(bikeModel);
        }

        bikeReference.child(bikeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Bike bike = dataSnapshot.getValue(Bike.class);

                    if (bike != null) {
                        // Populate UI elements with bike data
                        binding.bikeNametxt.setText(bike.getBikeName());
                        binding.bikeBrandtxt.setText(bike.getBikeBrand());
                        binding.bikeModeltxt.setText(bike.getBikeModel());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(BikeProfileEdit.this, "Failed to retrieve bike data", Toast.LENGTH_SHORT).show();
            }
        });


        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bikename = binding.bikeNametxt.getText().toString().trim();
                String bikebrand= binding.bikeBrandtxt.getText().toString();
                String bikemodel= binding.bikeModeltxt.getText().toString();


                updateData(bikename, bikebrand, bikemodel);
            }

            private void updateData(String bikename, String bikebrand, String bikemodel) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bikes").child(firebaseUser.getUid())
                        .child(bikeId);

                HashMap<String , Object> hashMap = new HashMap<>();
                hashMap.put("bikeName", bikename);
                hashMap.put("bikeBrand" , bikebrand);
                hashMap.put("bikeModel" , bikemodel);


                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Update completed", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Update failed.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
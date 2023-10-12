package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cyclecare.databinding.ActivityBikeRegBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BikeRegActivity extends AppCompatActivity {

    ActivityBikeRegBinding binding;
    FirebaseUser firebaseUser;
    String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBikeRegBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            profileId = firebaseUser.getUid();
            // Rest of your code using userId
        } else {
            // Handle the case where the user is not signed in
            // You might want to redirect the user to the sign-in screen or handle it appropriately.
        }



        binding.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBike();
            }
        });

    }

    private void registerBike() {
        String bikeName, bikeModel, bikeBrand;
        bikeName = String.valueOf(binding.bikeNametxt.getText()).trim();
        bikeModel = String.valueOf(binding.bikeModeltxt.getText()).trim();
        bikeBrand = String.valueOf(binding.bikeBrandtxt.getText()).trim();

        if (bikeName.isEmpty()){
            binding.bikeNametxt.setError("email is required");
            binding.bikeNametxt.requestFocus();
            return;
        }

        if (bikeModel.isEmpty()){
            binding.bikeModeltxt.setError("email is required");
            binding.bikeModeltxt.requestFocus();
            return;
        }

        if (bikeBrand.isEmpty()){
            binding.bikeBrandtxt.setError("email is required");
            binding.bikeBrandtxt.requestFocus();
            return;
        }
        else{
            createBikeAccount();
        }
    }

    private void createBikeAccount() {
        String bikeName = String.valueOf(binding.bikeNametxt.getText()).trim();
        String bikeModel = String.valueOf(binding.bikeModeltxt.getText()).trim();
        String bikeBrand = String.valueOf(binding.bikeBrandtxt.getText()).trim();

        // Get a reference to the "bikes" node in the Firebase database
        DatabaseReference bikesRef = FirebaseDatabase.getInstance().getReference("Bikes").child(profileId);

        // Generate a unique key for the new bike entry
        String bikeId = bikesRef.push().getKey();

        // Create a HashMap to store bike information
        HashMap<String, Object> bikeMap = new HashMap<>();
        bikeMap.put("bikeId", bikeId);
        bikeMap.put("owner_id", firebaseUser.getUid());
        bikeMap.put("bikeName", bikeName);
        bikeMap.put("bikeModel", bikeModel);
        bikeMap.put("bikeBrand", bikeBrand);
        bikeMap.put("bikeImg", "");


        // Save the bike information to the database
        bikesRef.child(bikeId).setValue(bikeMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred during the save process
                    // For example, show an error message
                });
    }

}
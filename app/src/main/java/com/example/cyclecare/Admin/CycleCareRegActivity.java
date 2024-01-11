package com.example.cyclecare.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cyclecare.R;
import com.example.cyclecare.databinding.ActivityBikeRegBinding;
import com.example.cyclecare.databinding.ActivityCycleCareRegBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CycleCareRegActivity extends AppCompatActivity {

    ActivityCycleCareRegBinding binding;
    FirebaseUser firebaseUser;
    String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCycleCareRegBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            profileId = firebaseUser.getUid();
            // Rest of your code using userId
        }


        binding.regCycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCycleCare();
            }
        });
    }



    private void addCycleCare() {
        String unitName, unitNumber, unitLocation, receiverName, dateReceived, installerName, dateInstalled;
        unitName = String.valueOf(binding.unitNametxt.getText()).trim();
        unitNumber = String.valueOf(binding.unitNumtxt.getText()).trim();
        unitLocation = String.valueOf(binding.locationtxt.getText()).trim();
        receiverName = String.valueOf(binding.receivertxt.getText()).trim();
        dateReceived = String.valueOf(binding.dateRecievedtxt.getText()).trim();
        installerName= String.valueOf(binding.installerNametxt.getText()).trim();
        dateInstalled= String.valueOf(binding.dateInstalledtxt.getText()).trim();


        if (unitName.isEmpty()){
            binding.unitNametxt.setError("email is required");
            binding.unitNametxt.requestFocus();
            return;
        }   if (unitNumber.isEmpty()){
            binding.unitNumtxt.setError("email is required");
            binding.unitNumtxt.requestFocus();
            return;
        }   if (unitLocation.isEmpty()){
            binding.locationtxt.setError("email is required");
            binding.locationtxt.requestFocus();
            return;
        }   if (receiverName.isEmpty()){
            binding.receivertxt.setError("email is required");
            binding.receivertxt.requestFocus();
            return;
        }   if (dateReceived.isEmpty()){
            binding.dateRecievedtxt.setError("email is required");
            binding.dateRecievedtxt.requestFocus();
            return;
        }   if (installerName.isEmpty()){
            binding.installerNametxt.setError("email is required");
            binding.installerNametxt.requestFocus();
            return;
        } if (dateInstalled.isEmpty()){
            binding.dateInstalledtxt.setError("email is required");
            binding.dateInstalledtxt.requestFocus();
            return;
        }else{
            registerCycleCare();
        }

    }

    private void registerCycleCare() {

        String unitName, unitNumber, unitLocation, receiverName, dateReceived, installerName, dateInstalled;
        unitName = String.valueOf(binding.unitNametxt.getText()).trim();
        unitNumber = String.valueOf(binding.unitNumtxt.getText()).trim();
        unitLocation = String.valueOf(binding.locationtxt.getText()).trim();
        receiverName = String.valueOf(binding.receivertxt.getText()).trim();
        dateReceived = String.valueOf(binding.dateRecievedtxt.getText()).trim();
        installerName= String.valueOf(binding.installerNametxt.getText()).trim();
        dateInstalled= String.valueOf(binding.dateInstalledtxt.getText()).trim();


        // Get a reference to the "bikes" node in the Firebase database
        DatabaseReference bikesRef = FirebaseDatabase.getInstance().getReference("CycleCareUnit");
        DatabaseReference lockRef = FirebaseDatabase.getInstance().getReference("LockActuator");

        boolean motionstate = false;
        boolean sensorstate = false;
        boolean isoccupied = false;

        // Generate a unique key for the new bike entry
        String cyclecareId = bikesRef.push().getKey();
        String lockId = bikesRef.push().getKey();

        // Create a HashMap to store bike information
        HashMap<String, Object> bikeMap = new HashMap<>();
        bikeMap.put("unitID", cyclecareId);
        bikeMap.put("unitName", unitName);
        bikeMap.put("unitNumber",unitNumber); //tentative
        bikeMap.put("unitLocation", unitLocation);
        bikeMap.put("receiverName", receiverName);
        bikeMap.put("dateRecieved", dateReceived);
        bikeMap.put("installerName", installerName);
        bikeMap.put("dateInstalled", dateInstalled);
        bikeMap.put("lockId", lockId);
        bikeMap.put("sensorId", lockId);
        bikeMap.put("isoccupied", isoccupied);
        bikeMap.put("sensorstate", sensorstate );


        // Save the bike information to the database
        bikesRef.child(cyclecareId).setValue(bikeMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Registered sucessfully", Toast.LENGTH_SHORT).show();;
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred during the save process
                    // For example, show an error message
                });

        // Create a HashMap to store bike information
        HashMap<String, Object> lockMap = new HashMap<>();
        lockMap.put("lockId", lockId);
        lockMap.put("state", motionstate );

        // Save the bike information to the database
        lockRef.child(lockId).setValue(lockMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Registered sucessfully", Toast.LENGTH_SHORT).show();;
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred during the save process
                    // For example, show an error message
                });


    }

}
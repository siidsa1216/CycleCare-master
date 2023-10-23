package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.cyclecare.databinding.ActivityConnectCycleCareBinding;


public class ConnectCycleCareActivity extends AppCompatActivity {

    ActivityConnectCycleCareBinding binding;
    String bikeId, bikeName, desc, bikeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectCycleCareBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        if (intent != null) {
            bikeId = intent.getStringExtra("bikeId");
           bikeName = intent.getStringExtra("bikeName");
           desc = intent.getStringExtra("bikeDesc");
           bikeImg = intent.getStringExtra("itemPhotoUrl"); ///laterrr

            binding.bikeName.setText(bikeName);
            binding.bikeDesc.setText(desc);
        }

            binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code
            }
        });


        binding.connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConnectCycleCareActivity.this, SelectCyccleCareActivity.class));
            }
        });
    }
}
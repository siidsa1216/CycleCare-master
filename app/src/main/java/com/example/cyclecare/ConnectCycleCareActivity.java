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

        binding.connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConnectCycleCareActivity.this, SelectCyccleCareActivity.class));
            }
        });
    }
}
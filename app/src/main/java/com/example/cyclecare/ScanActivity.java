package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cyclecare.databinding.ActivityQractivityBinding;
import com.example.cyclecare.databinding.ActivityScanBinding;

public class ScanActivity extends AppCompatActivity {

    ActivityScanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =   ActivityScanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScanActivity.this, LockActivity.class));
            }
        });
    }
}
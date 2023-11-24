package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.cyclecare.databinding.ActivityQractivityBinding;
import com.example.cyclecare.databinding.ActivityScanBinding;

import org.w3c.dom.Text;

public class ScanActivity extends AppCompatActivity {

    ActivityScanBinding binding;
    String cycleCareId, parkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        if (intent != null) {
            parkId = intent.getStringExtra("parkId");
            cycleCareId = intent.getStringExtra("cycleCareId");
        }


        binding.scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the QR code scanning logic
                // Assuming you have a ScanningQRActivity for scanning QR codes
                Intent intent = new Intent(ScanActivity.this, ScanningQRActivity.class);
                intent.putExtra("parkId", parkId);
                intent.putExtra("cycleCareId", cycleCareId);
                startActivity(intent);
            }
        });
    }
}
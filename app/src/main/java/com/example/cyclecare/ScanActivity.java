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
    public static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =   ActivityScanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        textView = findViewById(R.id.text2);
        // Inside your ScanActivity
//        Intent intent = getIntent();
//        if (intent.hasExtra("qrCodeBitmap")) {
//            Bitmap qrCodeBitmap = intent.getParcelableExtra("qrCodeBitmap");
//            Log.d("ScanActivity", "Got QR Code Bitmap from intent");
//            binding.qrView.setImageBitmap(qrCodeBitmap);
//        } else {
//            Log.e("ScanActivity", "Intent does not have QR Code Bitmap extra");
//        }

        binding.scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanningQRActivity.class));
            }
        });
    }
}
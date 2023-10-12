package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cyclecare.databinding.ActivityEditProfileBinding;
import com.example.cyclecare.databinding.ActivityPresecurityBinding;

public class PresecurityActivity extends AppCompatActivity {

    ActivityPresecurityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPresecurityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PresecurityActivity.this, SecurityQuestionsActivity.class));
                finish();
            }
        });

    }
}
package com.example.cyclecare;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cyclecare.Fragments.HomeFragment;
import com.example.cyclecare.Fragments.ProfileFragment;
import com.example.cyclecare.databinding.ActivityBikeRegBinding;
import com.example.cyclecare.databinding.ActivityViewStatusBinding;

public class ViewStatusActivity extends AppCompatActivity {

    ActivityViewStatusBinding binding;
    String parkId, cycleCareId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewStatusBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Intent intent = getIntent();
        if (intent != null) {
            parkId = intent.getStringExtra("parkId");
            cycleCareId = intent.getStringExtra("cycleCareId");
        }


        binding.statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewStatusActivity.this, MainActivity.class);
                intent.putExtra("parkId", parkId);
                intent.putExtra("cycleCareId", cycleCareId);
                startActivity(intent);



               // ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });

    }
}
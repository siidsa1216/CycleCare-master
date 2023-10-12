package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.cyclecare.Fragments.BikeProfileFragment;
import com.example.cyclecare.Fragments.HomeFragment;
import com.example.cyclecare.Fragments.MapFragment;
import com.example.cyclecare.Fragments.ParkFragment;
import com.example.cyclecare.Fragments.ProfileFragment;
import com.example.cyclecare.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item ->  {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.notif:
                    replaceFragment(new MapFragment());
                    break;
                case R.id.park:
                    replaceFragment(new ParkFragment());
                    break;
                case R.id.myBike:
                    replaceFragment(new BikeProfileFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
package com.example.cyclecare.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.cyclecare.Admin.Fragments.HomeAdminFragment;
import com.example.cyclecare.Admin.Fragments.ParkAdminFragment;
import com.example.cyclecare.Admin.Fragments.ReportsFragment;
import com.example.cyclecare.Admin.Fragments.UsersListFragment;
import com.example.cyclecare.Fragments.BikeProfileFragment;
import com.example.cyclecare.Fragments.MapFragment;
import com.example.cyclecare.Fragments.ParkFragment;
import com.example.cyclecare.Fragments.ProfileFragment;
import com.example.cyclecare.R;
import com.example.cyclecare.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends AppCompatActivity {
    
    ActivityAdminMainBinding binding;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeAdminFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item ->  {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeAdminFragment());
                    break;
                case R.id.park:
                    replaceFragment(new ParkAdminFragment());
                    break;
                case R.id.users:
                    replaceFragment(new UsersListFragment());
                    break;
                case R.id.reports:
                    replaceFragment(new ReportsFragment());
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
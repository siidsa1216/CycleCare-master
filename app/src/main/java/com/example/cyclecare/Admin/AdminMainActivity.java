package com.example.cyclecare.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
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

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AdminMainActivity extends AppCompatActivity {

    ActivityAdminMainBinding binding;
    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigation = binding.bottomNavigationView;

        replaceFragment(new HomeAdminFragment());
        bottomNavigation.show(1, true);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.bike_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_supervised_user_circle_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.report_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.profile_icon));

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()){
                    case 1:
                        replaceFragment(new HomeAdminFragment());
                        break;
                    case 2:
                        replaceFragment(new ParkAdminFragment());
                        break;
                    case 3:
                        replaceFragment(new UsersListFragment());
                        break;
                    case 4:
                        replaceFragment(new ReportsFragment());
                        break;
                    case 5:
                        replaceFragment(new ProfileFragment());
                        break;
                }
                return  null;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
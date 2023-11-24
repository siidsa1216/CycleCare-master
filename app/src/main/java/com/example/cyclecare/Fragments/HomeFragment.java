package com.example.cyclecare.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyclecare.Adapter.ParkAdapter;
import com.example.cyclecare.HomeViewPager;
import com.example.cyclecare.Model.Park;
import com.example.cyclecare.Model.User;
import com.example.cyclecare.R;
import com.example.cyclecare.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ViewPager slideViewPager;
    LinearLayout indicatorLayout;
    TextView[] dots;
    HomeViewPager viewPagerAdapter;
    String userId;
    private String username;

    List<Park> parkList;
    RecyclerView recyclerView;
    ParkAdapter parkAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
        }

        recyclerView = binding.recyclerViewPark;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        parkList = new ArrayList<>();
        parkAdapter = new ParkAdapter(getContext(), parkList);
        recyclerView.setAdapter(parkAdapter);

        readPark();

        slideViewPager =(ViewPager) binding.sliderViewPages;
        indicatorLayout = (LinearLayout) binding.indicator;

        viewPagerAdapter = new HomeViewPager(getContext());

        slideViewPager.setAdapter(viewPagerAdapter);


        showUserProfile();
        setUpIndicators(0);


        slideViewPager.addOnPageChangeListener(viewListener);


        return view;
    }


    private void readPark() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Park");

        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parkList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Park park = dataSnapshot.getValue(Park.class);
                    parkList.add(park);
                }

                toggleDefaultTextVisibility();

                parkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void toggleDefaultTextVisibility() {
        LinearLayout defaultText = binding.defaultText;
        if (parkList.isEmpty()) {
            defaultText.setVisibility(View.VISIBLE);
        } else {
            defaultText.setVisibility(View.GONE);
        }
    }


    private void showUserProfile() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User users = snapshot.getValue(User.class);

                if (users != null){

                    if (users.getProfilePicUrl().equals("")){
                        binding.profileImage.setImageResource(R.drawable.logo);
                    }else{
                        Picasso.get().load(users.getProfilePicUrl()).placeholder(R.drawable.logo).into(binding.profileImage);
                    }

                    username = users.getUsername();

                    binding.userName.setText(username);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setUpIndicators(int position){

        dots = new TextView[3];
        indicatorLayout.removeAllViews();

        for (int i = 0; i <dots.length; i++){

            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive, getContext().getTheme()));
            indicatorLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.active, getContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpIndicators(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem (int i){

        return slideViewPager.getCurrentItem() + i;
    }

}
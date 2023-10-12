package com.example.cyclecare.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cyclecare.Adapter.BikeAdapter;
import com.example.cyclecare.Model.Bike;
import com.example.cyclecare.R;
import com.example.cyclecare.databinding.FragmentBikeProfileBinding;
import com.example.cyclecare.databinding.FragmentParkBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ParkFragment extends Fragment {

    FragmentParkBinding binding;
    List<Bike> bikeList;
    BikeAdapter bikeAdapter;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;
    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentParkBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            profileId = firebaseUser.getUid();
        } else {
            // Handle the case where the user is not signed in
            // You might want to redirect the user to the sign-in screen or handle it appropriately.
        }

        recyclerView = binding.recyclerViewBike;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bikeList = new ArrayList<>();
        bikeAdapter = new BikeAdapter(getContext(), bikeList);
        recyclerView.setAdapter(bikeAdapter);

        readBike();

        return view;
    }

    private void readBike() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bikes").child(profileId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bikeList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Bike bike = dataSnapshot.getValue(Bike.class);
                    bikeList.add(bike);
                }
                bikeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
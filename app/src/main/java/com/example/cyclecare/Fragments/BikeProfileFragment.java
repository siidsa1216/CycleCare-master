package com.example.cyclecare.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cyclecare.Adapter.BikeAdapter;
import com.example.cyclecare.BikeRegActivity;
import com.example.cyclecare.Model.Bike;
import com.example.cyclecare.R;
import com.example.cyclecare.databinding.FragmentBikeProfileBinding;
import com.example.cyclecare.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BikeProfileFragment extends Fragment {

    FragmentBikeProfileBinding binding;
    List<Bike> bikeList;
    BikeAdapter bikeAdapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBikeProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = binding.recyclerViewBikes;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bikeList = new ArrayList<>();
        bikeAdapter = new BikeAdapter(getContext(), bikeList);
        recyclerView.setAdapter(bikeAdapter);

        readBike();



        binding.addBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BikeRegActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }

    private void readBike() {

        String profileId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
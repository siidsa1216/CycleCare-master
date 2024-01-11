package com.example.cyclecare.Admin.Fragments;

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
import com.example.cyclecare.Adapter.CycleCareUnitAdapter;
import com.example.cyclecare.Admin.CycleCareRegActivity;
import com.example.cyclecare.BikeRegActivity;
import com.example.cyclecare.ConnectCycleCareActivity;
import com.example.cyclecare.Model.Bike;
import com.example.cyclecare.Model.CycleCare;
import com.example.cyclecare.R;
import com.example.cyclecare.databinding.ActivityPresecurityBinding;
import com.example.cyclecare.databinding.FragmentBikeProfileBinding;
import com.example.cyclecare.databinding.FragmentParkAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParkAdminFragment extends Fragment {

    FragmentParkAdminBinding binding;
    List<CycleCare> cycleCareList;
    RecyclerView recyclerView;
    CycleCareUnitAdapter cycleCareUnitAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentParkAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = binding.recyclerViewCycleCare;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cycleCareList = new ArrayList<>();
        cycleCareUnitAdapter = new CycleCareUnitAdapter(getContext(), cycleCareList, true);
        recyclerView.setAdapter(cycleCareUnitAdapter);

        readCycleCare();

        binding.addUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CycleCareRegActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }

    private void  readCycleCare() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CycleCareUnit");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cycleCareList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    CycleCare cycleCare = dataSnapshot.getValue(CycleCare.class);
                    cycleCareList.add(cycleCare);
                }
                cycleCareUnitAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
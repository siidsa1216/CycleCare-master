package com.example.cyclecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.cyclecare.Adapter.CycleCareUnitAdapter;
import com.example.cyclecare.Model.CycleCare;
import com.example.cyclecare.databinding.ActivityConnectCycleCareBinding;
import com.example.cyclecare.databinding.ActivitySelectCyccleCareBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectCyccleCareActivity extends AppCompatActivity {

    ActivitySelectCyccleCareBinding binding;
    List<CycleCare> cycleCareList;
    RecyclerView recyclerView;
    CycleCareUnitAdapter cycleCareUnitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectCyccleCareBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recyclerView = binding.recyclerViewCycleCare;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cycleCareList = new ArrayList<>();
        cycleCareUnitAdapter = new CycleCareUnitAdapter(this, cycleCareList, false);
        recyclerView.setAdapter(cycleCareUnitAdapter);

        readCycleCare();
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
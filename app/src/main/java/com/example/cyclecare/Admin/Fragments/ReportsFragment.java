package com.example.cyclecare.Admin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cyclecare.Adapter.ParkAdapter;
import com.example.cyclecare.Adapter.ReportAdapter;
import com.example.cyclecare.Model.Park;
import com.example.cyclecare.Model.Report;
import com.example.cyclecare.Model.User;
import com.example.cyclecare.R;
import com.example.cyclecare.databinding.FragmentHomeAdminBinding;
import com.example.cyclecare.databinding.FragmentReportsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends Fragment {

    FragmentReportsBinding binding;

    List<Report> reportList;
    RecyclerView recyclerView;
    ReportAdapter reportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = binding.recyclerViewReports;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(getContext(), reportList);
        recyclerView.setAdapter(reportAdapter);


        readReports();

        return view;
    }

    private void readReports() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reports");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Iterate through each user's reports
                    for (DataSnapshot reportSnapshot : userSnapshot.getChildren()) {
                        Report report = reportSnapshot.getValue(Report.class);
                        reportList.add(report);
                    }
                }

                reportAdapter.notifyDataSetChanged();
              //  toggleDefaultTextVisibility();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

}
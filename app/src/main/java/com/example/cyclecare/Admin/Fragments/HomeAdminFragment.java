package com.example.cyclecare.Admin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cyclecare.Model.User;
import com.example.cyclecare.R;
import com.example.cyclecare.databinding.FragmentHomeAdminBinding;
import com.example.cyclecare.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeAdminFragment extends Fragment {

    FragmentHomeAdminBinding binding;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
        }


        getUserCount();
        getReportCount();
        getPSCOunt();
        showUserProfile();





        return view;

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

                    String username;
                    
                    username = users.getUsername();

                    binding.userName.setText(username);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPSCOunt() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("CycleCareUnit"); // Replace with the path to your node

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long count = snapshot.getChildrenCount();

                    binding.countpstxt.setText(String.valueOf(count));
                } else {
                    // Node does not exist
                    System.out.println("Node does not exist.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                System.out.println("Error: " + databaseError.getMessage());
            }
        });

    }

    private void getReportCount() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Reports"); // Replace with the path to your node

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long count = snapshot.getChildrenCount();

                    binding.countreporttxt.setText(String.valueOf(count));
                } else {
                    // Node does not exist
                    System.out.println("Node does not exist.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                System.out.println("Error: " + databaseError.getMessage());
            }
        });

    }

    private void getUserCount() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users"); // Replace with the path to your node

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long count = snapshot.getChildrenCount();

                    binding.countusertxt.setText(String.valueOf(count));
                } else {
                    // Node does not exist
                    System.out.println("Node does not exist.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                System.out.println("Error: " + databaseError.getMessage());
            }
        });

    }
}
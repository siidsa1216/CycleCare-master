package com.example.cyclecare.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyclecare.EditProfileActivity;
import com.example.cyclecare.Model.User;
import com.example.cyclecare.R;
import com.example.cyclecare.Signup;
import com.example.cyclecare.databinding.FragmentProfileBinding;
import com.example.cyclecare.login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.FileReader;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String username, email, phoneNum, password, profilePicUrl; //use to retrieve data from db
    String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
        } else {
            // Handle the case where the user is not signed in
            // You might want to redirect the user to the sign-in screen or handle it appropriately.
        }

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        });

        showUserProfile();
        return view;
    }


    private void showUserProfile() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User users = snapshot.getValue(User.class);

//                if (users != null){
//
//                if (users.getProfilePicUrl().equals("")){
//                    binding.profileImage.setImageResource(R.drawable.logo);
//               }else{
//                   Picasso.get().load(users.getProfilePicUrl()).placeholder(R.drawable.logo).into(binding.profileImage);
//                }
//
//                    username = users.getUsername();
//                    email = users.getEmail();
//                    phoneNum = users.getPhoneNum();
//
//                    binding.userProfileName.setText(username);
//                    binding.userProfileEmail.setText(email);
//                    binding.userProfilePhoneNum.setText(phoneNum);
//
//                }

                if (users != null) {
                    if (users.getProfilePicUrl().equals("")) {
                        binding.profileImage.setImageResource(R.drawable.logo);
                    } else {
                        Picasso.get().load(users.getProfilePicUrl()).placeholder(R.drawable.logo).into(binding.profileImage);
                    }

                    username = users.getUsername();
                    email = users.getEmail();
                    phoneNum = users.getPhoneNum();

                    binding.userProfileName.setText(username);
                    binding.userProfileEmail.setText(email);
                    binding.userProfilePhoneNum.setText(phoneNum);
                }

                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //signuot
    private void signout() {
        mAuth.signOut();
        Intent intent = new Intent(getContext(), login.class);
        startActivity(intent);
    }
}
package com.example.cyclecare.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyclecare.EditProfileActivity;
import com.example.cyclecare.LoadingDialog;
import com.example.cyclecare.LogoutDialog;
import com.example.cyclecare.Model.User;
import com.example.cyclecare.R;
import com.example.cyclecare.SettingActivity;
import com.example.cyclecare.Signup;
import com.example.cyclecare.databinding.FragmentProfileBinding;
import com.example.cyclecare.login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String username, email, phoneNum, password, profilePicUrl; //use to retrieve data from db
    String userId;

    LogoutDialog loadingDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadingDialog = new LogoutDialog(getActivity());

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

        binding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });

        binding.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportDialog();
            }
        });

        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLogoutActivity();
            }
        });

        showUserProfile();
        return view;
    }

    private void showReportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View reportDialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialogreport, null);
        builder.setTitle("Report Content")
                .setMessage("Please provide details about the issue:")
                .setView(reportDialogView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText reportEditText = reportDialogView.findViewById(R.id.reportEditText);
                        // Get the report details from the EditText
                        String reportDetails = reportEditText.getText().toString().trim();

                        if (!reportDetails.isEmpty()) {
                            // Call the method to submit the report
                            submitReport(reportDetails);
                        } else {
                            Toast.makeText(getContext(), "Please provide report details", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled the report, do nothing
                    }
                })
                .show();
    }

    private void submitReport(String reportDetails) {

        // Set the time zone to Philippine time
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Manila");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(timeZone);
        String timestampFormatted = sdf.format(new Date());



        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("Reports").child(userId);
        String reportId = reportsRef.push().getKey();

        // Create a HashMap to store report details
        Map<String, Object> reportMap = new HashMap<>();
        reportMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        reportMap.put("details", reportDetails);
        reportMap.put("timestamp", timestampFormatted); // Use ServerValue.TIMESTAMP

        reportsRef.child(reportId).setValue(reportMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Report submitted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to submit report", Toast.LENGTH_SHORT).show();
                        // Log the error for debugging
                        Log.e("ReportSubmission", "Error: " + e.getMessage());
                    }
                });
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
//    private void signout() {
//        mAuth.signOut();
//        Intent intent = new Intent(getContext(), login.class);
//        startActivity(intent);
//    }
}
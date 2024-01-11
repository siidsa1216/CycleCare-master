package com.example.cyclecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.cyclecare.Admin.AdminMainActivity;
import com.example.cyclecare.Model.User;
import com.google.firebase.FirebaseApp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.splashscreen);

        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already signed in
        if (mAuth.getCurrentUser() != null) {
            checkUserRole();
        } else {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreen.this, login.class));
                finish();
            }, 3000);
        }
    }

    private void checkUserRole() {
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        userRef.child("usertype").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userType = dataSnapshot.getValue(String.class);
                    handleUserType(userType);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleUserType(String userType) {
        if ("admin".equals(userType)) {
            // Redirect to admin panel
            startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
        } else {
            // Check if user answered verification questions
            checkIfUserAlreadyAnswered();
        }
        finish();
    }

    private void checkIfUserAlreadyAnswered() {
        DatabaseReference verificationQuestionsRef = FirebaseDatabase.getInstance()
                .getReference("VerificationQuestions").child(mAuth.getCurrentUser().getUid());

        verificationQuestionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), PresecurityActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}

package com.example.cyclecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyclecare.Admin.AdminMainActivity;
import com.example.cyclecare.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    ImageView backBtn;
    TextView noAccount, forgotPass;
    EditText emailtxt, passwordtxt;
    Button loginBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    private int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final long COOLDOWN_TIME = 5 * 60 * 1000; // 5 minutes in milliseconds
    private boolean isCooldownActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backBtn = findViewById(R.id.backBtn);
        noAccount = findViewById(R.id.noAccount);
        mAuth = FirebaseAuth.getInstance();
        emailtxt = findViewById(R.id.emailtxt);
        loginBtn = findViewById(R.id.loginBtn);
        passwordtxt = findViewById(R.id.passwordtxt);
        progressBar = findViewById(R.id.progressBar);
        forgotPass = findViewById(R.id.forgotPass);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCooldownActive) {
                    // Display a message to the user indicating that the account is locked
                    Toast.makeText(login.this, "Account locked. Try again later.", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                String email, password;
                email = String.valueOf(emailtxt.getText());
                password = String.valueOf(passwordtxt.getText());

                if (email.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    emailtxt.setError("Field is required");
                    emailtxt.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    passwordtxt.setError("Field is required");
                    passwordtxt.requestFocus();
                    return;
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                checkUserRole();
                            } else {
//                                progressBar.setVisibility(View.GONE);
//                                Toast.makeText(login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                handleFailedLogin();
                            }
                        }
                    });
                }
            }
        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, Signup.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, Forgot_Password.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, GetStarted.class));
            }
        });
    }

    private void handleFailedLogin() {

        progressBar.setVisibility(View.GONE);
        Toast.makeText(login.this, "Login failed.", Toast.LENGTH_SHORT).show();

        loginAttempts++;

        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            // Lock the account and start the cooldown period
            isCooldownActive = true;
            Toast.makeText(login.this, "Too many failed attempts. Account locked for 5 minutes.", Toast.LENGTH_SHORT).show();

            // Schedule a task to reset login attempts and cooldown status after 5 minutes
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginAttempts = 0;
                    isCooldownActive = false;
                }
            }, COOLDOWN_TIME);
        }
    }


    private void checkUserRole() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        userRef.child("usertype").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() instanceof String) {
                    String userType = dataSnapshot.getValue(String.class);
                    handleUserType(userType);
                } else {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        handleUserType(user.getUsertype());
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(login.this, "User object is null.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(login.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleUserType(String userType) {
        if ("admin".equals(userType)) {
            // Redirect to admin panel
            startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
        } else {
            // Redirect to main activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        finish();
    }


}

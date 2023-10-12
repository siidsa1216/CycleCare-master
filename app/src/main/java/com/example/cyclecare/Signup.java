package com.example.cyclecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyclecare.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    ImageView backBtn;
    TextView noAccount;
    EditText usernametxt, emailtxt, phoneNumtxt, passwordtxt;
    Button regBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    // creating a variable for
    // our object class
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        backBtn = findViewById(R.id.backBtn);
        noAccount =  findViewById(R.id.noAccount);
        usernametxt = findViewById(R.id.usernametxt);
        emailtxt = findViewById(R.id.emailtxt);
        phoneNumtxt = findViewById(R.id.phoneNumtxt);
        passwordtxt =findViewById(R.id.passwordtxt);
        regBtn = findViewById(R.id.regBtn);
        progressBar = findViewById(R.id.progressBar);
        user = new User(); // initializing our object
                            // class variable.

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


        //this is for back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, login.class));
            }
        });
        //this is for if the user wants to sign up
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, login.class));
            }
        });

    }

    private void registerUser() {
        String username, email, phoneNum, password;
        username = String.valueOf(usernametxt.getText()).trim();
        email = String.valueOf(emailtxt.getText());
        phoneNum = String.valueOf(phoneNumtxt.getText());
        password = String.valueOf(passwordtxt.getText());

        if (username.isEmpty()){
            usernametxt.setError("username is required");
            usernametxt.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (username.length() < 2){
            usernametxt.setError("username is too short");
            usernametxt.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (email.isEmpty()){
            emailtxt.setError("email is required");
            emailtxt.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtxt.setError("Please provide a valid email");
            emailtxt.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        else if (password.isEmpty()){
            passwordtxt.setError("Password is required.");
            passwordtxt.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        else if (password.length() < 6){
            Toast.makeText(Signup.this, "Password too weak", Toast.LENGTH_SHORT).show();
            passwordtxt.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        else{
            createUser(username, email,phoneNum, password);
        }

    }

    private void createUser(String username, String email, String phoneNum, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String, Object> user = new HashMap<>();
                user.put("id", mAuth.getCurrentUser().getUid());
                user.put("email", email);
                user.put("password", password); //for testing purposes only
                user.put("username", username);
                user.put("phoneNum",phoneNum);
                user.put("profilePicUrl", "");
                user.put("usertype", "user");
                user.put("status", "Active");

                firebaseDatabase = FirebaseDatabase.getInstance();
                reference = firebaseDatabase.getReference("Users");

                reference.child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Signup.this, "Signup successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), PresecurityActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(Signup.this, "Signup failed. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
package com.example.cyclecare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    ImageView backBtn;
    TextView noAccount;
    TextInputEditText usernametxt, emailtxt, phoneNumtxt, passwordtxt;
    Button regBtn;
    FirebaseAuth mAuth;
    LoadingDialog loadingDialog;
    String userId;

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
        loadingDialog = new LoadingDialog(Signup.this);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username, email, phoneNum, password;
                username = String.valueOf(usernametxt.getText()).trim();
                email = String.valueOf(emailtxt.getText());

                // Define the pattern for a Philippine phone number
                String phoneNumberPattern = "((^(\\+)(\\d){12}$)|(^\\d{11}$))";

                phoneNum = String.valueOf(phoneNumtxt.getText());
                password = String.valueOf(passwordtxt.getText());


                if (username.isEmpty()){
                    usernametxt.setError("username is required");
                    usernametxt.requestFocus();
                    return;
                }

                if (username.length() < 3){
                    usernametxt.setError("username is too short");
                    usernametxt.requestFocus();
                    return;
                }

                if (email.isEmpty()){
                    emailtxt.setError("email is required");
                    emailtxt.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailtxt.setError("Please provide a valid email");
                    emailtxt.requestFocus();
                    return;
                }

                else if (password.isEmpty()){
                    passwordtxt.setError("Password is required.");
                    passwordtxt.requestFocus();
                    return;
                }

                if (!phoneNum.matches(phoneNumberPattern)) {
                    phoneNumtxt.setError("Please provide a valid Philippine phone number");
                    phoneNumtxt.requestFocus();
                    return;
                }

               if (password.length() < 6){
                    Toast.makeText(Signup.this, "Password too weak", Toast.LENGTH_SHORT).show();
                    passwordtxt.requestFocus();
                    return;
                }

                loadingDialog.startLoadingActivity();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            //send verification email

                            FirebaseUser muser = mAuth.getCurrentUser();
                            if (muser != null) {
                                muser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Signup.this, "Verification email Has been sent.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent" + e.getMessage());
                                    }
                                });
                            }

                            //store data on realtime database
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference userRef = firebaseDatabase.getReference().child("Users");

                            userId = mAuth.getCurrentUser().getUid();

                            HashMap<String, Object> user = new HashMap<>();
                            user.put("id", userId);
                            user.put("email", email);
                            user.put("password", password); //for testing purposes only
                            user.put("username", username);
                            user.put("phoneNum",phoneNum);
                            user.put("profilePicUrl", "");
                            user.put("usertype", "user");
                            user.put("status", "Active");

                            userRef.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: user Profile has been created for" + userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });

                            Intent intent = new Intent(Signup.this, login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(Signup.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }
                });


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

}
package com.example.cyclecare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    ImageView backBtn;
    TextView noAccount, termsandcon;
    TextInputLayout usernameLayout, emailtxtLayout, phoneNumtxtLayout, PasswordLayout;
    TextInputEditText usernametxt, emailtxt, phoneNumtxt, passwordtxt;
    Button regBtn;
    FirebaseAuth mAuth;
    LoadingDialog loadingDialog;
    String userId;
  //  TermsAndPrivacyDialog termsAndConDialog;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

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
        termsandcon = findViewById(R.id.termsandcontxt);
        usernameLayout = findViewById(R.id.usernameLayout);
        emailtxtLayout = findViewById(R.id.emailtxtLayout);
        phoneNumtxtLayout = findViewById(R.id.phoneNumtxtLayout);
        PasswordLayout = findViewById(R.id.PasswordLayout);

        loadingDialog = new LoadingDialog(Signup.this);
       // termsAndConDialog = new TermsAndConDialog(Signup.this);

        noAccount.setPaintFlags(noAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        termsandcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        usernametxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateUsername(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this case
            }
        });

        emailtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this case
            }
        });

        phoneNumtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePhoneNum(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this case
            }
        });


        passwordtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this case
            }
        });






        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username, email, phoneNum, password;
                username = String.valueOf(usernametxt.getText()).trim();
                email = String.valueOf(emailtxt.getText());

                phoneNum = String.valueOf(phoneNumtxt.getText());
                password = String.valueOf(passwordtxt.getText());


                // Field validations
                if (!validateUsername(username) || !validateEmail(email) || !validatePassword(password) || !validatePhoneNum(phoneNum)) {
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
                            user.put("fcmToken", "");


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

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            PasswordLayout.setError("Field can't be empty");
            return false;
        }

        if (!password.matches(".*[a-zA-Z].*")) {
            PasswordLayout.setError("Password must contain at least one letter");
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            PasswordLayout.setError("Password must contain at least one digit");
            return false;
        }


        if (!password.matches(".*[@#$%^&+=].*")) {
            PasswordLayout.setError("Password must contain at least one special character (@#$%^&+=)");
            return false;
        }

        if (!password.matches("\\S+")) {
            PasswordLayout.setError("Password cannot contain white spaces");
            return false;
        }

        if (password.length() < 8) {
            PasswordLayout.setError("Password must be at least 4 characters long");
            return false;
        }
        PasswordLayout.setError(null);
        return true;
    }

    private boolean validatePhoneNum(String phoneNum){
        String phoneNumberPattern = "^(09|\\+639)\\d{9}$"; // Accepts both +63 and 0 as prefixes

        if (!phoneNum.matches(phoneNumberPattern)) {
            phoneNumtxtLayout.setError("Please provide a valid Philippine phone number");
            return false;
        } else {
            phoneNumtxtLayout.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            emailtxtLayout.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailtxtLayout.setError("Please enter a valid email address");
            return false;
        } else {
            emailtxtLayout.setError(null);
            return true;
        }
    }

    private boolean validateUsername(String username) {
        if (username.isEmpty()) {
            usernameLayout.setError("Username is required");
            return false;
        } else if (username.length() < 5) {
            usernameLayout.setError("Username too short");
            return false;
        } else {
            usernameLayout.setError(null);  // Clear the error if the username is valid
            return true;
        }
    }
}
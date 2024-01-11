package com.example.cyclecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
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

import com.example.cyclecare.Admin.AdminMainActivity;
import com.example.cyclecare.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    TextInputLayout emailtxtLayout, PasswordLayout;
    TextInputEditText emailtxt, passwordtxt;
    Button loginBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backBtn = findViewById(R.id.backBtn);
        noAccount = findViewById(R.id.noAccount);
        mAuth = FirebaseAuth.getInstance();
        emailtxt = findViewById(R.id.emailtxt);
        loginBtn = findViewById(R.id.loginBtn);
        emailtxtLayout = findViewById(R.id.emailtxtLayout);
        PasswordLayout = findViewById(R.id.PasswordLayout);
        passwordtxt = findViewById(R.id.passwordtxt);
        progressBar = findViewById(R.id.progressBar);
        forgotPass = findViewById(R.id.forgotPass);
        loadingDialog = new LoadingDialog(login.this);

        noAccount.setPaintFlags(noAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


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


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, password;
                email = String.valueOf(emailtxt.getText());
                password = String.valueOf(passwordtxt.getText());

                // Field validations
                if (!validateEmail(email) || !validatePassword(password)) {
                    return;
                }

                loadingDialog.startLoadingActivity();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                checkUserRole();


                            } else {
                                //
                                Toast.makeText(login.this, "Incorrect password or account not found.", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();
                            }
                        }
                    });

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

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            PasswordLayout.setError("Field can't be empty");
            return false;
        }
        PasswordLayout.setError(null);
        return true;
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
            Toast.makeText(login.this, "Signup successful.", Toast.LENGTH_SHORT).show();

            checkIfAlreadyAnswered();

        }

        finish();    }

    private void checkIfAlreadyAnswered() {

// Reference to the VerificationQuestions node for the specific user
        DatabaseReference verificationQuestionsRef = FirebaseDatabase.getInstance()
                .getReference("VerificationQuestions").child(mAuth.getCurrentUser().getUid());

        verificationQuestionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the user exists in the VerificationQuestions node
                if (!dataSnapshot.exists()) {
                    // User exists, you can access the data
                    startActivity(new Intent(getApplicationContext(),PresecurityActivity.class));
                } else {
                    // User does not exist in the VerificationQuestions node
                    // You can perform actions accordingly
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

}

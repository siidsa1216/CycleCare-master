package com.example.cyclecare;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    ImageView backBtn;
    EditText emailtxt;
    Button confirm_email;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        backBtn = findViewById(R.id.backBtn);
        emailtxt = findViewById(R.id.emailtxt);
        confirm_email =findViewById(R.id.confirm_email);
        mAuth = FirebaseAuth.getInstance();

        confirm_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailtxt.getText().toString().isEmpty()){
                    Toast.makeText(Forgot_Password.this, "Please enter your email.", Toast.LENGTH_LONG).show();
                    emailtxt.setError("Email address is required.");
                    emailtxt.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(emailtxt.getText().toString()).matches()){
                    Toast.makeText(Forgot_Password.this, "Please re-enter your email address.", Toast.LENGTH_LONG).show();
                    emailtxt.setError("Valid email address is required.");
                    emailtxt.requestFocus();
                }
                else {
                    forgotpass(emailtxt.getText().toString());
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity( new Intent(Forgot_Password.this, login.class));}
        });
    }

    private void forgotpass(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Email Sent.");

                    showAlertDialog();
                }
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Forgot_Password.this);
        builder.setTitle("Password Reset Confirmation");
        builder.setMessage("We've sent an email to reset your password.Please check your inbox. Thank you!");
        //close
        builder.setPositiveButton("Return to Log-in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Forgot_Password.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SecurityQuestionsActivity extends AppCompatActivity {

    private Spinner spinner, spinner1, spinner2;
    private EditText editTextAnswer1, editTextAnswer2, editTextAnswer3;
    private Button submitBtn;

    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_questions);

        spinner = findViewById(R.id.spinner);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        editTextAnswer1 = findViewById(R.id.answerEditText);
        editTextAnswer2 = findViewById(R.id.answerEditText1);
        editTextAnswer3 = findViewById(R.id.answerEditText2);
        submitBtn = findViewById(R.id.submitBtn);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("VerificationQuestions");

        if (firebaseUser != null) {
            profileId = firebaseUser.getUid();
            // Rest of your code using userId
        } else {
            // Handle the case where the user is not signed in
            // You might want to redirect the user to the sign-in screen or handle it appropriately.
        }

        setupSpinners();
        submitBtn.setOnClickListener(v -> saveToFirebase());
    }


    private void setupSpinners() {
        // Create separate string arrays for each set of questions
        String[] questionsArray1 = getResources().getStringArray(R.array.questions_array1);
        String[] questionsArray2 = getResources().getStringArray(R.array.questions_array2);
        String[] questionsArray3 = getResources().getStringArray(R.array.questions_array3);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.custom_spinner_item, questionsArray1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.custom_spinner_item, questionsArray2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, R.layout.custom_spinner_item, questionsArray3);

        adapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        adapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        adapter3.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        spinner.setAdapter(adapter1);
        spinner1.setAdapter(adapter2);
        spinner2.setAdapter(adapter3);

        spinner.setOnItemSelectedListener(new SpinnerItemSelected(editTextAnswer1));
        spinner1.setOnItemSelectedListener(new SpinnerItemSelected(editTextAnswer2));
        spinner2.setOnItemSelectedListener(new SpinnerItemSelected(editTextAnswer3));
    }


    private void saveToFirebase() {
        String answer1 = editTextAnswer1.getText().toString().trim();
        String answer2 = editTextAnswer2.getText().toString().trim();
        String answer3 = editTextAnswer3.getText().toString().trim();

        if (!answer1.isEmpty() && !answer2.isEmpty() && !answer3.isEmpty()) {
            String question1 = spinner.getSelectedItem().toString();
            String question2 = spinner1.getSelectedItem().toString();
            String question3 = spinner2.getSelectedItem().toString();


            // Get a reference to the "bikes" node in the Firebase database
            DatabaseReference quesRef =  databaseReference.child(profileId);

            // Generate a unique key for the new bike entry
            String quesId =  quesRef.push().getKey();
            // Create a HashMap to store the answers
            HashMap<String, String> answersMap = new HashMap<>();
            answersMap.put("question1", question1);
            answersMap.put("answer1", answer1);

            answersMap.put("question2", question2);
            answersMap.put("answer2", answer2);

            answersMap.put("question3", question3);
            answersMap.put("answer3", answer3);

            // Save the HashMap to Firebase
            quesRef.setValue(answersMap);

            // Save the bike information to the database
            quesRef.child(quesId).setValue(answersMap)
                    .addOnSuccessListener(aVoid -> {
                        showSuccessDialog();
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors that occurred during the save process
                        // For example, show an error message
                    });

    }
    }

    private void showSuccessDialog() {

        ConstraintLayout successConstraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(SecurityQuestionsActivity.this).inflate(R.layout.securityquestionlayoutdialog, successConstraintLayout);

        // Use the inflated view to find the button
        Button successDone = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(SecurityQuestionsActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        successDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                startActivity(new Intent(SecurityQuestionsActivity.this, MainActivity.class));
                finish();
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private class SpinnerItemSelected implements AdapterView.OnItemSelectedListener {
        private EditText associatedEditText;

        SpinnerItemSelected(EditText editText) {
            this.associatedEditText = editText;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Handle spinner item selected
            // You can customize this if needed
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing here
        }
    }
}
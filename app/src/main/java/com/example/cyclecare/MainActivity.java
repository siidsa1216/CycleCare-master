package com.example.cyclecare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.cyclecare.Fragments.BikeProfileFragment;
import com.example.cyclecare.Fragments.HomeFragment;
import com.example.cyclecare.Fragments.ParkFragment;
import com.example.cyclecare.Fragments.ProfileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import com.pusher.pushnotifications.PushNotifications;
public class MainActivity extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;
    TextView sendVerificationtxt;
    Button sendVerification;
    FirebaseAuth mAuth;
    String userId;
    CardView cardSendVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("new_user_forums");
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();


        bottomNavigation = findViewById(R.id.bottomNavigationView);
        sendVerification = findViewById(R.id.sendVerification);
        sendVerificationtxt = findViewById(R.id.sendVerificationtxt);
        cardSendVerification = findViewById(R.id.cardsendverication);

        userId = mAuth.getCurrentUser().getUid();
        FirebaseUser user = mAuth.getCurrentUser();

        if (!user.isEmailVerified()){
            cardSendVerification.setVisibility(View.VISIBLE);
            sendVerificationtxt.setVisibility(View.VISIBLE);
            sendVerification.setVisibility(View.VISIBLE);

            sendVerification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Verification email Has been sent.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Email not sent" + e.getMessage());
                            }
                        });
                    }
            });
        }

        replaceFragment(new HomeFragment());
        bottomNavigation.show(1, true);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.parking_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.bike_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.profile_icon));

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()){
                    case 1:
                        replaceFragment(new HomeFragment());
                        break;
                    case 2:
                        replaceFragment(new ParkFragment());
                        break;
                    case 3:
                        replaceFragment(new BikeProfileFragment());
                        break;
                    case 4:
                        replaceFragment(new ProfileFragment());
                        break;
                }
                return  null;
            }
        });


        getFCMToken();

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit CycleCare app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If user clicks "Yes", exit the app
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If user clicks "No", dismiss the dialog and do nothing
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                String token = task.getResult();

                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);

                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("fcmToken").setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }

        });
    }

}


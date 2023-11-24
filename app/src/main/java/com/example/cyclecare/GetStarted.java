package com.example.cyclecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class GetStarted extends AppCompatActivity {

    ViewPager slideViewPager;
    LinearLayout indicatorLayout;
    TextView skipBtn;
    Button getStarted;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;
    Animation animation;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getstarted);

        mAuth = FirebaseAuth.getInstance();

        //check if the user is already signed in
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        skipBtn = findViewById(R.id.skipBtn);
        getStarted = findViewById(R.id.getStartedBtn);


        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStarted.this, login.class));
                finish();
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //skip btn
                startActivity(new Intent(GetStarted.this, login.class));
                finish();
            }
        });

        slideViewPager =(ViewPager) findViewById(R.id.sliderViewPage);
        indicatorLayout = (LinearLayout) findViewById(R.id.indicator);

        viewPagerAdapter = new ViewPagerAdapter(this);

        slideViewPager.setAdapter(viewPagerAdapter);

        setUpIndicators(0);

        slideViewPager.addOnPageChangeListener(viewListener);
    }

    public void setUpIndicators(int position){
        dots = new TextView[3];
        indicatorLayout.removeAllViews();

        for (int i = 0; i <dots.length; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive, getApplicationContext().getTheme()));
            indicatorLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.active, getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpIndicators(position);

            if (position == 0){
                getStarted.setVisibility(View.INVISIBLE);
            }
            else if (position == 1){
                getStarted.setVisibility(View.INVISIBLE);
            }
            else{
                animation = AnimationUtils.loadAnimation(GetStarted.this, R.anim.bouncy_anim);
                getStarted.setAnimation(animation);
                getStarted.setVisibility(View.VISIBLE);
                skipBtn.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
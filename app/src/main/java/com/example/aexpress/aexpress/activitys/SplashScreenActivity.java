package com.example.aexpress.aexpress.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.aexpress.R;
import com.example.aexpress.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {
  ActivitySplashScreenBinding binding;
  Animation top,bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        top = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.mainlogo);
        bottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.sublogo);

        binding.mainlogo.setAnimation(top);
        binding.sublogoorange.setAnimation(bottom);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        },5000);
    }
}
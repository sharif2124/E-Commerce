package com.example.aexpress.aexpress.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.aexpress.R;
import com.example.aexpress.aexpress.utilits.Constants;
import com.example.aexpress.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {
   ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String ordercode = getIntent().getStringExtra("orderCode");
        binding.webview.setMixedContentAllowed(true);
        binding.webview.loadUrl(Constants.PAYMENT_URL+ordercode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
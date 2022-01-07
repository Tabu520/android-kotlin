package com.taipt.l4userinteraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.taipt.l4userinteraction.databinding.ActivityOrderBinding;

public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String message = "Order: " + intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        binding.txtOrder.setText(message);
    }
}
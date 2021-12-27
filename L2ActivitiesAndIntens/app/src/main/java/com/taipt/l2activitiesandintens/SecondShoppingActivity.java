package com.taipt.l2activitiesandintens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.taipt.l2activitiesandintens.databinding.ActivitySecondShoppingBinding;

public class SecondShoppingActivity extends AppCompatActivity {


    private static final String LOG_TAG = SecondShoppingActivity.class.getSimpleName();

    private ActivitySecondShoppingBinding binding;
    public static final String ITEM_VALUE = "com.taipt.l2activitiesandintens.ITEM_VALUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondShoppingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnItem1.setOnClickListener(v -> returnItemValue(binding.btnItem1.getText().toString()));
        binding.btnItem2.setOnClickListener(v -> returnItemValue(binding.btnItem2.getText().toString()));
        binding.btnItem3.setOnClickListener(v -> returnItemValue(binding.btnItem3.getText().toString()));
        binding.btnItem4.setOnClickListener(v -> returnItemValue(binding.btnItem4.getText().toString()));
        binding.btnItem5.setOnClickListener(v -> returnItemValue(binding.btnItem5.getText().toString()));
        binding.btnItem6.setOnClickListener(v -> returnItemValue(binding.btnItem6.getText().toString()));
    }

    public void returnItemValue(String itemValue) {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(ITEM_VALUE, itemValue);
        setResult(RESULT_OK, replyIntent);
        Log.d(LOG_TAG, "End SecondActivity");
        finish();
    }
}
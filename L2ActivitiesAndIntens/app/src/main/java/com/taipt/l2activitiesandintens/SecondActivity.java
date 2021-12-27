package com.taipt.l2activitiesandintens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.taipt.l2activitiesandintens.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    private static final String LOG_TAG = SecondActivity.class.getSimpleName();
    public static final String EXTRA_REPLY = "com.taipt.l2activitiesandintens.EXTRA_REPLY";

    private ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(LOG_TAG, "-------");
        Log.d(LOG_TAG, "onCreate");

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        binding.textMessage.setText(message);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    public void returnReply(View view) {
        String reply = binding.editTextSecond.getText().toString();
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, reply);
        setResult(RESULT_OK, replyIntent);
        Log.d(LOG_TAG, "End SecondActivity");
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
}
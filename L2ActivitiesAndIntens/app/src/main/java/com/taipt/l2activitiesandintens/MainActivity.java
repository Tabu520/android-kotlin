package com.taipt.l2activitiesandintens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.taipt.l2activitiesandintens.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;

    public static final String EXTRA_MESSAGE = "com.taipt.l2activitiesandintens.MESSAGE";
    public static final int TEXT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            boolean isVisible = savedInstanceState.getBoolean("reply_visible");
            if (isVisible) {
                binding.textHeaderReply.setVisibility(View.VISIBLE);
                binding.textMessageReply.setText(savedInstanceState.getString("reply_text"));
                binding.textMessageReply.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (binding.textHeaderReply.getVisibility() == View.VISIBLE) {
            outState.putBoolean("reply_visible", true);
            outState.putString("reply_text", binding.textMessageReply.getText().toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Test for the right intent reply.
        if (requestCode == TEXT_REQUEST) {
            // Test to make sure the intent reply result was good.
            if (resultCode == RESULT_OK) {
                String reply = data.getStringExtra(SecondActivity.EXTRA_REPLY);

                // Make the reply head visible.
                binding.textHeaderReply.setVisibility(View.VISIBLE);

                // Set the reply and make it visible.
                binding.textMessageReply.setText(reply);
                binding.textMessageReply.setVisibility(View.VISIBLE);
            }
        }
    }

    public void launchSecondActivity(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, SecondActivity.class);
        String message;
        switch (view.getId()) {
            case R.id.button_main:
                message = binding.editTextMain.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivityForResult(intent, TEXT_REQUEST);
                break;
            case R.id.button_one:
                message = binding.buttonOne.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
            case R.id.button_two:
                message = binding.buttonTwo.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
            case R.id.button_three:
                message = binding.buttonThree.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
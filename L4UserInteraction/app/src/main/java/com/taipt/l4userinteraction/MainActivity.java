package com.taipt.l4userinteraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.taipt.l4userinteraction.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.taipt.l4userinteraction.MESSAGE";

    private ActivityMainBinding binding;

    private String mOrderMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderActivity.class);
            intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
            startActivity(intent);
        });
    }

    public void displayToast(String message) {
        mOrderMessage = message;
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showDonutOrder(View view) {
        displayToast(getString(R.string.donut_order_message));
    }

    public void showIceCreamOrder(View view) {
        displayToast(getString(R.string.ice_cream_order_message));
    }

    public void showFroyoOrder(View view) {
        displayToast(getString(R.string.froyo_order_message));
    }
}
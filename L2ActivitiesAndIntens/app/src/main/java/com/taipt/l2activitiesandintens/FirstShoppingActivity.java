package com.taipt.l2activitiesandintens;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.taipt.l2activitiesandintens.databinding.ActivityFirstShoppingBinding;

public class FirstShoppingActivity extends AppCompatActivity {

    private ActivityFirstShoppingBinding binding;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    String itemValue = "";
                    if (intent != null) {
                        itemValue = intent.getStringExtra(SecondShoppingActivity.ITEM_VALUE);
                    }
                    binding.txt1.setText(itemValue);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstShoppingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            String itemValue = savedInstanceState.getString("item_1");
            binding.txt1.setText(itemValue);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("item_1", binding.txt1.getText().toString());
    }

    public void launchSecondShoppingActivity(View view) {
        mStartForResult.launch(new Intent(this, SecondShoppingActivity.class));
    }

    public void findStoreLocation(View view) {
        String loc = binding.edtStoreLocation.getText().toString();
        Uri addressUri = Uri.parse("geo:0,0?q=" + loc);
        Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }
}
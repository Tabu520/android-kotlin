package com.taipt.listexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.taipt.listexample.databinding.ActivityMainBinding;
import com.taipt.listexample.expandablelist.ExpandableListActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnExpandableList.setOnClickListener( view -> {
            Intent intent = new Intent(this, ExpandableListActivity.class);
            startActivity(intent);
        });
    }
}
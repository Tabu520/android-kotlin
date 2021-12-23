package com.taipt.newsapplicationjava.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.taipt.newsapplicationjava.R;
import com.taipt.newsapplicationjava.databinding.ActivityNewsDetaillBinding;

public class NewsDetailActivity extends AppCompatActivity {

    private ActivityNewsDetaillBinding binding;

    String title, description, content, imageUrl, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetaillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        content = getIntent().getStringExtra("content");
        imageUrl = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");
    }
}
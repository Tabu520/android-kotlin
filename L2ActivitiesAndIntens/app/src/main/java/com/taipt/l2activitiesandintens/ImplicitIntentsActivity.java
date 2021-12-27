package com.taipt.l2activitiesandintens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.taipt.l2activitiesandintens.databinding.ActivityImplicitIntentsBinding;

public class ImplicitIntentsActivity extends AppCompatActivity {

    private ActivityImplicitIntentsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImplicitIntentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void openWebsite(View view) {
        String url = binding.edtWebsite.getText().toString();
        if (!url.contains("https://") || !url.contains("http://")) {
            url = "https://" + url;
        }
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this!");
        }
    }

    public void openLocation(View view) {
        String loc = binding.edtLocation.getText().toString();
        Uri addressUri = Uri.parse("geo:0,0?q=" + loc);
        Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }

    public void shareText(View view) {
        String text = binding.edtShare.getText().toString();
        String mimeType = "text/plain";
        new ShareCompat.IntentBuilder(this)
                .setType(mimeType)
                .setChooserTitle("Share this text with: ")
                .setText(text)
                .startChooser();
    }
}
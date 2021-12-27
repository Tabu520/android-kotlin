package com.taipt.listexample.expandablelist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.taipt.listexample.databinding.ActivityExpandableListBinding;

public class ExpandableListActivity extends AppCompatActivity implements ILoadContentCallback {

    private ActivityExpandableListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpandableListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new LoadContentAsync(this, this).execute();
    }

    @Override
    public void onLoadContentSuccess(MainContentModel mainContentModel) {
        if (mainContentModel != null) {
            PhoneListAdapter phoneListAdapter = new PhoneListAdapter(this, mainContentModel.getCompanies());
            binding.phoneList.setAdapter(phoneListAdapter);
        }
    }
}

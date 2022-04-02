package com.taipt.viewpager2java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CollectionDemoFragment fragment = CollectionDemoFragment.newInstance();

        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.fragment_container);
        FragmentTransaction fmTransaction = fm.beginTransaction();
        if (currentFragment == null) {
            fmTransaction.add(R.id.fragment_container, fragment);
        } else {
            fmTransaction.replace(R.id.fragment_container, fragment);
        }
        fmTransaction.commit();
    }
}
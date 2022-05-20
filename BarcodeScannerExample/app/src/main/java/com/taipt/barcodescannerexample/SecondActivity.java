package com.taipt.barcodescannerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    Userinfo userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView data = findViewById(R.id.data);
        userinfo = (Userinfo) getIntent().getSerializableExtra("serialzable");
        String name = userinfo.getName();
        String phone = userinfo.getPhone();
        data.setText("Your entered name is "+name+" number is "+phone);
    }
}
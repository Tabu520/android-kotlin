package com.taipt.barcodescannerexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText name;
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        Button send = findViewById(R.id.send);
        send.setOnClickListener(v -> {
            if (TextUtils.isEmpty(name.getText().toString()) && TextUtils.isEmpty(phone.getText().toString())) {
                Toast.makeText(MainActivity.this,"Something is wrong kindly check", Toast.LENGTH_LONG).show();
            }else{
                sendUserData(name.getText().toString(),phone.getText().toString());
            }
        });

    }

    private void sendUserData(String username, String userPhone) {
        Userinfo userinfo = new Userinfo();
        userinfo.setName(username);
        userinfo.setPhone(userPhone);
        Intent send = new Intent(MainActivity.this,SecondActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("serialzable",userinfo);
        send.putExtras(b);
        startActivity(send);
    }
}
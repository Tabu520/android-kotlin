package com.taipt.l4userinteraction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.taipt.l4userinteraction.databinding.ActivityMainBinding;
import com.taipt.l4userinteraction.databinding.ContentMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.taipt.l4userinteraction.MESSAGE";

    private ContentMainBinding contentMainBinding;

    private String mOrderMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        contentMainBinding = binding.contentMain;

        setSupportActionBar(binding.toolbar);

        assert binding.fab != null;
        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderActivity.class);
            intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
            startActivity(intent);
        });
        if (contentMainBinding.edtPhoneNumber != null) {
            contentMainBinding.edtPhoneNumber.setOnEditorActionListener((v, actionId, event) -> {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Toast.makeText(this, "clicked send", Toast.LENGTH_SHORT).show();
                    dialNumber();
                    handled = true;
                }
                return handled;
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_order:
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
                startActivity(intent);
                return true;
            case R.id.action_status:
                displayToast(getString(R.string.action_status_message));
                return true;
            case R.id.action_favorites:
                displayToast(getString(R.string.action_favorites_message));
                return true;
            case R.id.action_contact:
                displayToast(getString(R.string.action_contact_message));
                return true;
            case R.id.action_date_picker:
                startActivity(new Intent(MainActivity.this, DatePickerActivity.class));
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
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

    @SuppressLint("QueryPermissionsNeeded")
    private void dialNumber() {
        String phoneNum = null;
        // If the editText field is not null,
        // concatenate "tel: " with the phone number string.
        if (contentMainBinding.edtPhoneNumber != null) phoneNum = "tel:" +
                contentMainBinding.edtPhoneNumber.getText().toString();
        // Specify the intent.
        Intent intent = new Intent(Intent.ACTION_DIAL);
        // Set the data for the intent as the phone number.
        intent.setData(Uri.parse(phoneNum));
        // If the intent resolves to a package (app),
        // start the activity with the intent.
        startActivity(intent);
    }
}
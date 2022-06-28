package com.jonbott.knownspies.Activities.Details;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jonbott.knownspies.Activities.SecretDetails.SecretDetailsActivity;
import com.jonbott.knownspies.Dependencies.DependencyRegistry;
import com.jonbott.knownspies.Helpers.Constants;
import com.jonbott.knownspies.R;

public class SpyDetailsActivity extends AppCompatActivity {

    private SpyDetailsPresenter presenter;

    private ImageView profileImage;
    private TextView nameTextView;
    private TextView ageTextView;
    private TextView genderTextView;
    private ImageButton calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spy_details);
        attachUI();

        Bundle bundle = getIntent().getExtras();
        DependencyRegistry.shared.inject(this, bundle);
    }

    public void configureWith(SpyDetailsPresenter presenter) {
        this.presenter = presenter;
        ageTextView.setText(presenter.getAge());
        nameTextView.setText(presenter.getName());
        genderTextView.setText(presenter.getGender());
        profileImage.setImageResource(presenter.getImageId());
    }

    //region UI Methods

    private void attachUI() {
        profileImage = findViewById(R.id.details_profile_image);
        nameTextView = findViewById(R.id.details_name);
        ageTextView = findViewById(R.id.details_age);
        genderTextView = findViewById(R.id.details_gender);
        calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(v -> gotoSecretDetails());
    }

    //endregion UI Methods

    //region Navigation

    private void gotoSecretDetails() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.spyIdKey, presenter.getSpyId());

        Intent intent = new Intent(SpyDetailsActivity.this, SecretDetailsActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    //endregion
}

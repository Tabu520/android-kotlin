package com.jonbott.knownspies.Activities.SecretDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jonbott.knownspies.Activities.SpyList.SpyListActivity;
import com.jonbott.knownspies.Coordinators.RootCoordinator;
import com.jonbott.knownspies.Dependencies.DependencyRegistry;
import com.jonbott.knownspies.R;

public class SecretDetailsActivity extends AppCompatActivity {

    SecretDetailsPresenter presenter;
    private RootCoordinator coordinator;

    ProgressBar progressBar;
    TextView crackingLabel;
    Button finishedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_details);

        attachUI();
        Bundle bundle = getIntent().getExtras();
        DependencyRegistry.shared.inject(this, bundle);
    }

    public void configureWith(SecretDetailsPresenter presenter, RootCoordinator rootCoordinator) {
        this.presenter = presenter;
        coordinator = rootCoordinator;
        this.presenter.crackPassword(password -> {
            progressBar.setVisibility(View.GONE);
            crackingLabel.setText(presenter.getPassword());
        });
    }

    //region Helper Methods

    private void attachUI() {
        progressBar = findViewById(R.id.secret_progress_bar);
        crackingLabel = findViewById(R.id.secret_cracking_label);
        finishedButton = findViewById(R.id.secret_finished_button);

        finishedButton.setOnClickListener(v -> finishedClicked());

    }

    //endregion

    //region User Interaction

    private void finishedClicked() {
        coordinator.handleFinishedClicked(this);
    }

    //endregion

}

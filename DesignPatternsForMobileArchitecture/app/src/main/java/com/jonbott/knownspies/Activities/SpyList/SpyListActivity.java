package com.jonbott.knownspies.Activities.SpyList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jonbott.knownspies.Activities.Details.SpyDetailsActivity;
import com.jonbott.knownspies.Coordinators.RootCoordinator;
import com.jonbott.knownspies.Dependencies.DependencyRegistry;
import com.jonbott.knownspies.Helpers.Constants;
import com.jonbott.knownspies.ModelLayer.DTOs.SpyDTO;
import com.jonbott.knownspies.ModelLayer.Enums.Source;
import com.jonbott.knownspies.R;

import java.util.ArrayList;
import java.util.List;

public class SpyListActivity extends AppCompatActivity {

    private static final String TAG = "SpyListActivity";

    private SpyListPresenter spyListPresenter;
    private RootCoordinator coordinator;
    //    private List<SpyDTO> spies = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spy_list);

        attachUI();

        DependencyRegistry.shared.inject(this);
    }

    public void configureWith(SpyListPresenter presenter, RootCoordinator rootCoordinator) {
        this.spyListPresenter = presenter;
        coordinator = rootCoordinator;

        loadData();
        setupObservables();
    }

    @SuppressLint("CheckResult")
    private void setupObservables() {
        spyListPresenter.spies().subscribe(spies -> {
            SpyViewAdapter adapter = (SpyViewAdapter) recyclerView.getAdapter();
            adapter.setSpies(spies);
        });
    }

    //region Helper Methods
    private void attachUI() {

        Button newSpyButton = findViewById(R.id.new_spy_button);
        newSpyButton.setOnClickListener(v -> {
            spyListPresenter.addNewSpy();
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.spy_recycler_view);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        initializeListView();
    }

    private void loadData() {
        spyListPresenter.loadData(this::onDataReceived);
    }

    //endregion

    //region User Interaction

    private void rowTapped(int position) {
        SpyDTO spy = spyListPresenter.spies().getValue().get(position);
        gotoSpyDetails(spy.id);
    }

    private void onDataReceived(Source source) {
        String message = String.format("Data from %s", source.name());
        Toast.makeText(SpyListActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    //endregion

    //region List View Adapter

    private void initializeListView() {
        SpyViewAdapter adapter = new SpyViewAdapter((v, position) -> rowTapped(position));
        recyclerView.setAdapter(adapter);
    }

    //endregion

    //region Navigation

    private void gotoSpyDetails(int spyId) {

        coordinator.handleSpyCellTapped(this, spyId);
    }

    //endregion

}

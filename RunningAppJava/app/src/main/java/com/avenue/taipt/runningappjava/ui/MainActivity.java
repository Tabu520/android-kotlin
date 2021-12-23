package com.avenue.taipt.runningappjava.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.avenue.taipt.runningappjava.R;
import com.avenue.taipt.runningappjava.databinding.ActivityMainBinding;
import com.avenue.taipt.runningappjava.db.RunDao;
import com.avenue.taipt.runningappjava.executors.AppExecutors;
import com.avenue.taipt.runningappjava.other.Constants;
import com.avenue.taipt.runningappjava.repositories.MainRepository;
import com.avenue.taipt.runningappjava.ui.viewmodels.MainViewModel;
import com.avenue.taipt.runningappjava.ui.viewmodels.MainViewModelFactory;
import com.avenue.taipt.runningappjava.ui.viewmodels.StatisticsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Inject
    public RunDao runDao;

    @Inject
    public AppExecutors appExecutors;

    private MainViewModel mainViewModel;
    private StatisticsViewModel statisticsViewModel;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Timber.d("AppExecutors --- %s", appExecutors.hashCode());
        MainRepository mainRepository = new MainRepository(runDao);
        MainViewModelFactory viewModelProviderFactory = new MainViewModelFactory(mainRepository);
        mainViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel.class);
        statisticsViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(StatisticsViewModel.class);

        setSupportActionBar(binding.toolbar);

        BottomNavigationView bottomNavView = binding.bottomNavigationView;
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.runFragment, R.id.statisticsFragment, R.id.settingsFragment)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavView, navController);
        bottomNavView.setOnItemReselectedListener(item -> {
            /* No-OP */
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.settingsFragment:
                case R.id.runFragment:
                case R.id.statisticsFragment:
                    binding.bottomNavigationView.setVisibility(View.VISIBLE);
                    break;
                default:
                    binding.bottomNavigationView.setVisibility(View.GONE);
                    break;
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        navigateToTrackingFragmentIfNeeded(intent);
    }

    private void navigateToTrackingFragmentIfNeeded(Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(Constants.ACTION_SHOW_TRACKING_FRAGMENT)) {
                navController.navigate(R.id.action_global_trackingFragment);
            }
        }
    }

    public MainViewModel getMainViewModel() {
        return this.mainViewModel;
    }

    public StatisticsViewModel getStatisticsViewModel() {
        return this.statisticsViewModel;
    }
}
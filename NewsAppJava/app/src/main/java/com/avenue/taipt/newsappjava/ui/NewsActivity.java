package com.avenue.taipt.newsappjava.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.avenue.taipt.newsappjava.R;
import com.avenue.taipt.newsappjava.databinding.ActivityNewsBinding;
import com.avenue.taipt.newsappjava.db.ArticleDatabase;
import com.avenue.taipt.newsappjava.repository.NewsRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewsActivity extends AppCompatActivity {

    private ActivityNewsBinding binding;

    private NewsViewModel newsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        NewsRepository newsRepository = new NewsRepository(ArticleDatabase.createDatabase(this));
        NewsViewModelProviderFactory viewModelProviderFactory = new NewsViewModelProviderFactory(getApplication(), newsRepository);
        newsViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel.class);

        BottomNavigationView bottomNavView = binding.bottomNavigationView;
        NavController navController = Navigation.findNavController(this ,R.id.newsNavHostFragment);
        NavigationUI.setupWithNavController(bottomNavView, navController);
    }

    public NewsViewModel getNewsViewModel() {
        return this.newsViewModel;
    }
}
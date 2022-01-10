package com.avenue.taipt.newsappjava.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.avenue.taipt.newsappjava.NewsApplication;
import com.avenue.taipt.newsappjava.R;
import com.avenue.taipt.newsappjava.databinding.ActivityNewsBinding;
import com.avenue.taipt.newsappjava.db.ArticleDatabase;
import com.avenue.taipt.newsappjava.repository.NewsRepository;
import com.avenue.taipt.newsappjava.utils.AppExecutors;
import com.avenue.taipt.newsappjava.utils.TEA;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewsActivity extends AppCompatActivity {

    private ActivityNewsBinding binding;

    public static String quote = "Now rise, and show your strength. Be eloquent, and deep, and tender; see, with a clear eye, into Nature, and into life:  spread your white wings of quivering thought, and soar, a god-like spirit, over the whirling world beneath you, up through long lanes of flaming stars to the gates of eternity!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TaiPT", "NewsActivity::onCreate()");
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ((NewsApplication) getApplication()).getNewsRepositoryInstance();
        NewsViewModelProviderFactory viewModelProviderFactory = new NewsViewModelProviderFactory(getApplication());
        NewsViewModel newsViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel.class);

        BottomNavigationView bottomNavView = binding.bottomNavigationView;
        NavController navController = Navigation.findNavController(this ,R.id.newsNavHostFragment);
        NavigationUI.setupWithNavController(bottomNavView, navController);

        /* Create a cipher using the first 16 bytes of the passphrase */
        TEA tea = new TEA("And is there honey still for tea?".getBytes());

        byte[] original = quote.getBytes();

        /* Run it through the cipher... and back */
        byte[] crypt = tea.encrypt(original);
        byte[] result = tea.decrypt(crypt);

        /* Ensure that all went well */
        String test = new String(result);
        Log.d("TaiPT", test);
        Log.d("TaiPT", new String(crypt));
        Log.d("TaiPT", String.valueOf(tea.getS()[0]));
        Log.d("TaiPT", String.valueOf(tea.getS()[1]));
        Log.d("TaiPT", String.valueOf(tea.getS()[2]));
        Log.d("TaiPT", String.valueOf(tea.getS()[3]));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((NewsApplication) getApplication()).clearNewsRepositoryInstance();
    }
}
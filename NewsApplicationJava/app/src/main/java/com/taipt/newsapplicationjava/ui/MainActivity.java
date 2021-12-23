package com.taipt.newsapplicationjava.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.taipt.newsapplicationjava.R;
import com.taipt.newsapplicationjava.adapters.CategoryAdapter;
import com.taipt.newsapplicationjava.adapters.NewsAdapter;
import com.taipt.newsapplicationjava.databinding.ActivityMainBinding;
import com.taipt.newsapplicationjava.models.Article;
import com.taipt.newsapplicationjava.models.Category;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryClickListener {

    private ActivityMainBinding binding;

    private ArrayList<Article> articles;
    private ArrayList<Category> categories;
    private CategoryAdapter categoryAdapter;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        articles = new ArrayList<>();
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categories);
        newsAdapter = new NewsAdapter(this, articles);
        categoryAdapter.setCategoryClickListener(this);

        binding.rvNews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCategories.setAdapter(categoryAdapter);
        binding.rvNews.setAdapter(newsAdapter);

        getCategories();
    }

    private void getCategories() {
        categories.add(new Category("All", ""));
        categories.add(new Category("Technology", "https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"));
        categories.add(new Category("Science", "https://images.unsplash.com/photo-1532094349884-543bc11b234d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"));
        categories.add(new Category("Sports", "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"));
        categories.add(new Category("General", "https://images.unsplash.com/photo-1432821596592-e2c18b78144f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"));
        categories.add(new Category("Business", "https://images.unsplash.com/photo-1554224155-6726b3ff858f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2511&q=80"));
        categories.add(new Category("Entertainment", "https://images.unsplash.com/photo-1603739903239-8b6e64c3b185?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2071&q=80"));
        categories.add(new Category("Health", "https://images.unsplash.com/photo-1494597564530-871f2b93ac55?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2013&q=80"));
        categoryAdapter.notifyDataSetChanged();
    }

    private void getNews() {

    }

    @Override
    public void onClickCategory(int position) {

    }
}
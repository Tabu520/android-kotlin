package com.avenue.taipt.newsappjava.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.avenue.taipt.newsappjava.databinding.FragmentArticleBinding;
import com.avenue.taipt.newsappjava.models.Article;
import com.avenue.taipt.newsappjava.ui.NewsViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ArticleFragment extends Fragment {

    private FragmentArticleBinding binding;
    private NewsViewModel viewModel;
    private Article article;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArticleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);
        article = new Article();
        if (getArguments() != null) {
            article = ArticleFragmentArgs.fromBundle(getArguments()).getArticle();
            binding.webView.setWebViewClient(new WebViewClient());
            binding.webView.loadUrl(article.getUrl());
        }
        binding.fab.setOnClickListener(v -> {
            viewModel.saveArticle(article);
            Snackbar.make(view, "Article saved successfully!", Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

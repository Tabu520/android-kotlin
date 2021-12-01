package com.avenue.taipt.newsappjava.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.avenue.taipt.newsappjava.databinding.FragmentArticleBinding;
import com.avenue.taipt.newsappjava.ui.NewsActivity;
import com.avenue.taipt.newsappjava.ui.NewsViewModel;

public class ArticleFragment extends Fragment {

    private FragmentArticleBinding binding;
    private NewsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArticleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ((NewsActivity) requireActivity()).getNewsViewModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

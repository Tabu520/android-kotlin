package com.avenue.taipt.newsappjava.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avenue.taipt.newsappjava.adapters.NewsAdapter;
import com.avenue.taipt.newsappjava.databinding.FragmentSavedNewsBinding;
import com.avenue.taipt.newsappjava.models.Article;
import com.avenue.taipt.newsappjava.ui.NewsActivity;
import com.avenue.taipt.newsappjava.ui.NewsViewModel;
import com.google.android.material.snackbar.Snackbar;

public class SavedNewsFragment extends Fragment {

    private FragmentSavedNewsBinding binding;
    private NewsViewModel newsViewModel;
    private NewsAdapter newsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newsViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);
        setupRecyclerView();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Article article = newsAdapter.getDiffer().getCurrentList().get(position);
                newsViewModel.deleteArticle(article);
                Snackbar.make(requireView(), "Successfully deleted article", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> newsViewModel.saveArticle(article)).show();
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback)
                .attachToRecyclerView(binding.rvSavedNews);

        newsViewModel.getSavedNews().observe(getViewLifecycleOwner(), articles -> {
            newsAdapter.getDiffer().submitList(articles);
        });
    }

    private void setupRecyclerView() {
        Log.d("TaiPT", "SavedNewsFragment::setupRecyclerView()");
        newsAdapter = new NewsAdapter();
        binding.rvSavedNews.setAdapter(newsAdapter);
        binding.rvSavedNews.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

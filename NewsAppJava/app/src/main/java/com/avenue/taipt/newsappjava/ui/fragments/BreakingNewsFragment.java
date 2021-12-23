package com.avenue.taipt.newsappjava.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avenue.taipt.newsappjava.R;
import com.avenue.taipt.newsappjava.adapters.NewsAdapter;
import com.avenue.taipt.newsappjava.databinding.FragmentBreakingNewsBinding;
import com.avenue.taipt.newsappjava.models.Article;
import com.avenue.taipt.newsappjava.models.NewsResponse;
import com.avenue.taipt.newsappjava.ui.NewsActivity;
import com.avenue.taipt.newsappjava.ui.NewsViewModel;
import com.avenue.taipt.newsappjava.utils.Constants;
import com.avenue.taipt.newsappjava.utils.Resource;

public class BreakingNewsFragment extends Fragment {

    private FragmentBreakingNewsBinding binding;
    private NewsViewModel newsViewModel;
    private NewsAdapter newsAdapter;

    private boolean isScrolling;
    private boolean isLoading;
    private boolean isLastPage;

    private final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            assert layoutManager != null;
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            boolean isNotLoadingAndNotLastPage = !isLoading && !isLastPage;
            boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
            boolean isNotAtBeginning = firstVisibleItemPosition >= 0;
            boolean isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE;
            boolean shouldPaginate =
                    isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling;
            if (shouldPaginate) {
                newsViewModel.loadBreakingNews("us");
                isScrolling = false;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TaiPT", "BreakingNewsFragment::onCreateView()");
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TaiPT", "BreakingNewsFragment::onViewCreated()");
        setupRecyclerView();
        newsViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);
        newsViewModel.loadBreakingNews("us");
        newsViewModel.breakingNews.observe(getViewLifecycleOwner(), newsResponseResource -> {
            if (newsResponseResource instanceof Resource.Loading) {
                showProgressionBar();
            } else if (newsResponseResource instanceof Resource.Success) {
                hideProgressionBar();
                if (newsResponseResource.getData() != null) {
                    newsAdapter.getDiffer().submitList(newsResponseResource.getData().getArticles());
                    int totalPages = newsResponseResource.getData().getTotalResults() / Constants.QUERY_PAGE_SIZE + 2;
                    isLastPage = newsViewModel.getBreakingNewsPage() == totalPages;
                    if (isLastPage) {
                        binding.rvBreakingNews.setPadding(0, 0, 0, 0);
                    }
                }
            } else if (newsResponseResource instanceof Resource.Error) {
                hideProgressionBar();
                if (newsResponseResource.getMessage() != null) {
                    Log.d("BreakingNewsFragment", "An error occurred " + newsResponseResource.getMessage());
                    Toast.makeText(requireContext(), "An error occurred " + newsResponseResource.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        newsAdapter.setArticleItemClickListener(article -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("article", article);
            Navigation.findNavController(view).navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle);
        });
    }

    private void setupRecyclerView() {
        Log.d("TaiPT", "BreakingNewsFragment::setupRecyclerView()");
        newsAdapter = new NewsAdapter();
        binding.rvBreakingNews.setAdapter(newsAdapter);
        binding.rvBreakingNews.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rvBreakingNews.addOnScrollListener(this.scrollListener);
    }

    private void hideProgressionBar() {
        binding.paginationProgressBar.setVisibility(View.GONE);
        isLoading = false;
    }

    private void showProgressionBar() {
        binding.paginationProgressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

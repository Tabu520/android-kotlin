package com.taipt.newsapplicationjava.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taipt.newsapplicationjava.R;
import com.taipt.newsapplicationjava.models.Article;
import com.taipt.newsapplicationjava.ui.NewsDetailActivity;

import java.util.ArrayList;
import java.util.Objects;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewViewHolder> {

    private Context context;
    private ArrayList<Article> articles;

    public NewsAdapter(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    private final DiffUtil.ItemCallback<Article> differCallback = new DiffUtil.ItemCallback<Article>() {
        @Override
        public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.getUrl().equals(newItem.getUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return Objects.equals(newItem, oldItem);
        }
    };

    private AsyncListDiffer<Article> differ = new AsyncListDiffer<>(this, differCallback);

    public AsyncListDiffer<Article> getDiffer() {
        return this.differ;
    }

    @NonNull
    @Override
    public ViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewViewHolder holder, int position) {
        Article article = differ.getCurrentList().get(position);
        holder.tvNewsHeading.setText(article.getTitle());
        holder.tvNewsSubtitle.setText(article.getDescription());
        Picasso.get().load(article.getUrlToImage()).into(holder.ivNews);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("title", article.getTitle());
            intent.putExtra("content", article.getContent());
            intent.putExtra("description", article.getDescription());
            intent.putExtra("image", article.getUrlToImage());
            intent.putExtra("url", article.getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (articles == null) ? 0 : articles.size();
    }

    public class ViewViewHolder extends RecyclerView.ViewHolder {

        TextView tvNewsHeading;
        TextView tvNewsSubtitle;
        ImageView ivNews;

        public ViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNewsHeading = itemView.findViewById(R.id.txt_news_heading);
            tvNewsSubtitle = itemView.findViewById(R.id.txt_news_subtitle);
            ivNews = itemView.findViewById(R.id.iv_news);
        }
    }
}

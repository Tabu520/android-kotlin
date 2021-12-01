package com.avenue.taipt.newsappjava.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.avenue.taipt.newsappjava.R;
import com.avenue.taipt.newsappjava.models.Article;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ArticleViewHolder> {

    public interface OnArticleItemClickListener {
        void onArticleItemClick(Article article);
    }

    public OnArticleItemClickListener articleItemClickListener;

    private DiffUtil.ItemCallback<Article> differCallback = new DiffUtil.ItemCallback<Article>() {
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

    @NonNull
    @Override
    public NewsAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_preview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ArticleViewHolder holder, int position) {
        Article article = differ.getCurrentList().get(position);
        Glide.with(holder.itemView).load(article.getUrlToImage()).into(holder.ivArticleImage);
        holder.tvSource.setText(article.getSource().getName());
        holder.tvTitle.setText(article.getTitle());
        holder.tvDescription.setText(article.getDescription());
        holder.tvPublishedAt.setText(article.getPublishedAt());
        holder.itemView.setOnClickListener(v -> {
            if (this.articleItemClickListener != null) {
                articleItemClickListener.onArticleItemClick(article);
            }
        });
    }

    public void setArticleItemClickListener(OnArticleItemClickListener articleItemClickListener) {
        this.articleItemClickListener = articleItemClickListener;
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        ImageView ivArticleImage;
        TextView tvSource;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPublishedAt;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivArticleImage = itemView.findViewById(R.id.ivArticleImage);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPublishedAt = itemView.findViewById(R.id.tvPublishedAt);
        }
    }
}

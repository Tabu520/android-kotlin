package com.taipt.newsapplicationjava.adapters;

import android.content.Context;
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
import com.taipt.newsapplicationjava.models.Category;

import java.util.ArrayList;
import java.util.Objects;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<Category> categories;
    private CategoryClickListener categoryClickListener;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    private final DiffUtil.ItemCallback<Category> differCallback = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getCategory().equals(newItem.getCategory());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return Objects.equals(newItem, oldItem);
        }
    };

    private AsyncListDiffer<Category> differ = new AsyncListDiffer<>(this, differCallback);

    public AsyncListDiffer<Category> getDiffer() {
        return this.differ;
    }

    public void setCategoryClickListener(CategoryClickListener categoryClickListener) {
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = differ.getCurrentList().get(position);
        holder.tvCategory.setText(category.getCategory());
        Picasso.get().load(category.getCategoryImageUrl()).into(holder.ivCategory);
        holder.itemView.setOnClickListener(view -> {
            categoryClickListener.onClickCategory(position);
        });
    }

    @Override
    public int getItemCount() {
        return (categories == null) ? 0 : categories.size();
    }

    public interface CategoryClickListener {
        void onClickCategory(int position);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategory;
        ImageView ivCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.txt_category);
            ivCategory = itemView.findViewById(R.id.iv_category);
        }
    }
}

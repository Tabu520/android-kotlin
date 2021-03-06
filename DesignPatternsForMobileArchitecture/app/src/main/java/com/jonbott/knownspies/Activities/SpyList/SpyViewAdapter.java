package com.jonbott.knownspies.Activities.SpyList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.jonbott.knownspies.Helpers.CustomItemClickListener;
import com.jonbott.knownspies.ModelLayer.DTOs.SpyDTO;
import com.jonbott.knownspies.ModelLayer.Database.Realm.Spy;
import com.jonbott.knownspies.R;

import java.util.ArrayList;
import java.util.List;

public class SpyViewAdapter extends RecyclerView.Adapter<SpyViewHolder> {

    List<SpyDTO> spies = new ArrayList<>();
    CustomItemClickListener listener;

    public SpyViewAdapter(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public SpyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View spyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spy_cell, parent, false);

        final SpyViewHolder spyViewHolder = new SpyViewHolder(spyView);

        spyView.setOnClickListener(v -> listener.onItemClick(v, spyViewHolder.getAbsoluteAdapterPosition()));

        return spyViewHolder;
    }

    @Override
    public void onBindViewHolder(SpyViewHolder holder, int index) {
        SpyDTO spy = spies.get(index);
        holder.configureWith(spy);
    }

    @Override
    public int getItemCount() {
        return spies.size();
    }

    public void setSpies(List<SpyDTO> spies) {
        this.spies = spies;
        notifyDataSetChanged();
    }
}

package com.jonbott.knownspies.Activities.SpyList;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jonbott.knownspies.Helpers.Helper;
import com.jonbott.knownspies.ModelLayer.DTOs.SpyDTO;
import com.jonbott.knownspies.ModelLayer.Database.Realm.Spy;
import com.jonbott.knownspies.R;

public class SpyViewHolder extends RecyclerView.ViewHolder {
    Context context;
    CardView cv;
    TextView personName;
    TextView personAge;
    ImageView personPhoto;

    public SpyViewHolder(View itemView) {
        super(itemView);

        this.context = itemView.getContext();
        this.cv = itemView.findViewById(R.id.card_view);
        this.personName = itemView.findViewById(R.id.person_name);
        this.personAge = itemView.findViewById(R.id.person_age);
        this.personPhoto = itemView.findViewById(R.id.person_photo);
    }

    public void configureWith(SpyDTO spy) {
        int imageId = Helper.resourceIdWith(context, spy.imageName);
        String age = String.valueOf(spy.age);

        personName.setText(spy.name);
        personAge.setText(age);
        personPhoto.setImageResource(imageId);
    }

}

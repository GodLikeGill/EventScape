package com.group5.eventscape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.pm.ShortcutXmlParser;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.eventscape.BuildConfig;
import com.group5.eventscape.R;
import com.group5.eventscape.databinding.CustomLayoutFavEventsBinding;
import com.group5.eventscape.databinding.CustomRowLayoutMyEventsBinding;
import com.group5.eventscape.interfaces.OnRowClicked;
import com.group5.eventscape.models.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.MyEventsViewHolder> {

    private final ArrayList<Event> events;
    CustomRowLayoutMyEventsBinding binding;
    private final OnRowClicked clickListener;

    public MyEventsAdapter( ArrayList<Event> events, OnRowClicked clickListener) {
        this.events = events;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyEventsAdapter.MyEventsViewHolder(CustomRowLayoutMyEventsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventsViewHolder holder, int position) {
        Event item = events.get(position);
        holder.bind( item, clickListener);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class MyEventsViewHolder extends RecyclerView.ViewHolder {
        CustomRowLayoutMyEventsBinding itemBinding;

        public MyEventsViewHolder(CustomRowLayoutMyEventsBinding binding){
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind( Event item, OnRowClicked clickListener) {

            itemBinding.tvMyEventsTitle.setText(item.getTitle());
            itemBinding.tvMyEventsLocation.setText(item.getAddress());
            itemBinding.tvMyEventsDateTime.setText(item.getDate());
            itemBinding.tvMyEventsPrice.setText("Price: $" + item.getPrice());

            Picasso.get().load(item.getImage()).into(itemBinding.circleMyEventsPicture);


            itemBinding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onRowClicked(item);
                }
            });
        }
    }
}
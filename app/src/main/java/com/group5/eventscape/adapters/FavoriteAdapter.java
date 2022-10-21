package com.group5.eventscape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.eventscape.databinding.CustomRowLayoutBinding;
import com.group5.eventscape.interfaces.OnRowClicked;
import com.group5.eventscape.models.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoritesViewHolder>{
    private final Context context;
    private ArrayList<Event> dataSourceArray;
    CustomRowLayoutBinding binding;
    private final OnRowClicked clickListener;

    public FavoriteAdapter(Context context, ArrayList<Event> dataSourceArray, OnRowClicked clickListener) {
        this.context = context;
        this.dataSourceArray = dataSourceArray;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoritesViewHolder(CustomRowLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Event item = dataSourceArray.get(position);
        holder.bind(context, item, clickListener);
    }

    @Override
    public int getItemCount() {
        return this.dataSourceArray.size();
    }


    public class FavoritesViewHolder extends RecyclerView.ViewHolder {
        CustomRowLayoutBinding itemBinding;

        public FavoritesViewHolder(CustomRowLayoutBinding binding){
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Context context, Event item, OnRowClicked clickListener) {
            itemBinding.tvEventTitle.setText(item.getTitle());
            itemBinding.tvLocation.setText(item.getAddress());

            itemBinding.tvEventDateTime.setText(item.getDate() + "\n" + item.getTime());
            Picasso.get().load(item.getImage())
                    .centerCrop()
                    .resize(120, 120)
                    .transform(new CropCircleTransformation())
                    .onlyScaleDown()
                    .into(itemBinding.imgThumb);


            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onRowClicked(item);
                }
            });
        }
    }
}

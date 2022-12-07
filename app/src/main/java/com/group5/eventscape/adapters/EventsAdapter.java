package com.group5.eventscape.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.eventscape.interfaces.OnRowClicked;
import com.group5.eventscape.databinding.CustomRowLayoutBinding;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.viewmodels.EventViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder>{
    private final Context context;
    private ArrayList<Event> dataSourceArray;
    CustomRowLayoutBinding binding;
    private final OnRowClicked clickListener;

    public EventsAdapter(Context context, ArrayList<Event> dataSourceArray, OnRowClicked clickListener) {
        this.context = context;
        this.dataSourceArray = dataSourceArray;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventsViewHolder(CustomRowLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        Event item = dataSourceArray.get(position);
        holder.bind(context, item, clickListener);
    }

    @Override
    public int getItemCount() {
        return this.dataSourceArray.size();
    }

    public void filteredList(ArrayList<Event> filteredList){
        dataSourceArray = filteredList;
        notifyDataSetChanged();
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder {
        CustomRowLayoutBinding itemBinding;

        public EventsViewHolder(CustomRowLayoutBinding binding){
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Context context, Event item, OnRowClicked clickListener) {
            itemBinding.tvEventTitle.setText(item.getTitle());
            itemBinding.tvLocation.setText(item.getAddress());
            String toDate = "";
            if(item.getDate2() != null){
                if(!item.getDate2().isEmpty()){
                    toDate = " to " + item.getDate2();
                }
            }

            itemBinding.tvEventDateTime.setText(item.getDate() + toDate + "\n" + convertTime(item.getTime()));
            Picasso.get().load(item.getImage())
                    .centerCrop()
                    .resize(120, 120)
                    .transform(new CropCircleTransformation())
                    .onlyScaleDown()
                    .into(itemBinding.imgThumb);

            if(!item.getCategory().isEmpty()){
                itemBinding.tvDistance.setText(item.getCategory());
            }

            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onRowClicked(item);
                }
            });
        }
    }

    public String convertTime(String time){
        String str = "";
        if(time != null){
            if(time != ""){
                String[] result = time.split(":");
                if(result[0] != "" && result[1] != ""){
                    Integer hour = Integer.parseInt(result[0]);
                    Integer minute = Integer.parseInt(result[1]);
                    str = ((hour > 12) ? hour % 12 : hour) + ":" + (minute < 10 ? ("0" + minute) : minute) + " " + ((hour >= 12) ? "PM" : "AM");
                }
            }
        }

        return str;
    }
}

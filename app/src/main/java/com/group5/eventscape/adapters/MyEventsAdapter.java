package com.group5.eventscape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.eventscape.R;
import com.group5.eventscape.models.Event;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.ViewHolder> {

    private Context context;
    private final List<Event> events;

    public MyEventsAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public MyEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(events.get(position).getTitle());
        holder.location.setText(events.get(position).getAddress());
        holder.date.setText(events.get(position).getDate());
        Picasso.get().load(events.get(position).getImage()).centerCrop()
                .resize(120, 120)
                .transform(new CropCircleTransformation())
                .onlyScaleDown().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView location;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            image = itemView.findViewById(R.id.imgThumb);
            title = itemView.findViewById(R.id.tvEventTitle);
            location = itemView.findViewById(R.id.tvLocation);
            date = itemView.findViewById(R.id.tvEventDateTime);
        }
    }
}

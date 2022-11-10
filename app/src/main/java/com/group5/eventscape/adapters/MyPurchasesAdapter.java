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
import com.group5.eventscape.models.Orders;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MyPurchasesAdapter extends RecyclerView.Adapter<MyPurchasesAdapter.ViewHolder> {

    private Context context;
    private final List<Orders> orders;

    public MyPurchasesAdapter(List<Orders> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public MyPurchasesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_layout_my_purchases, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(orders.get(position).getEventImageThumb()).centerCrop()
                .resize(120, 120)
                .transform(new CropCircleTransformation())
                .onlyScaleDown().into(holder.image);

        holder.title.setText(orders.get(position).getEventTitle());
        holder.location.setText(orders.get(position).getEventLocation());
        holder.orderDateTime.setText("Order date: " + parseDateToddMMyyyy(orders.get(position).getOrderDate()));
        holder.eventDate.setText("Event date: " + orders.get(position).getEventDate());
        holder.numberOfTickets.setText("Number of tickets: " + orders.get(position).getNumberOfTickets());
        holder.ticketPrice.setText("Ticket price: CA $" + String.format("%.2f", Double.parseDouble(orders.get(position).getTicketPrice()))  );
        holder.totalPurchasePrice.setText("Total price: CA $" + orders.get(position).getTotalOrderPrice());

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView location;
        TextView orderDateTime;
        TextView eventDate;
        TextView numberOfTickets;
        TextView ticketPrice;
        TextView totalPurchasePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            image = itemView.findViewById(R.id.imgThumb);
            title = itemView.findViewById(R.id.tvEventTitle);
            location = itemView.findViewById(R.id.tvLocation);
            orderDateTime = itemView.findViewById(R.id.tvPurchaseDateAndTime);
            eventDate = itemView.findViewById(R.id.tvEventDate);
            numberOfTickets = itemView.findViewById(R.id.tvNumberOfTickets);
            ticketPrice = itemView.findViewById(R.id.tvTicketPrice);
            totalPurchasePrice = itemView.findViewById(R.id.tvTotalPurchasePrice);
        }
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "EEE MMM dd HH:mm:ss z yyyy";
//        String outputPattern = "dd-MMM-yyyy h:mm a";
        String outputPattern = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
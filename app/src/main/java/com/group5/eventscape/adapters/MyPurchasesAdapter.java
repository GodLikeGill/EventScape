package com.group5.eventscape.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.eventscape.R;
import com.group5.eventscape.activities.PurchaseDetailActivity;
import com.group5.eventscape.models.Order;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyPurchasesAdapter extends RecyclerView.Adapter<MyPurchasesAdapter.ViewHolder> {

    private Context context;
    private final List<Order> orders;

    public MyPurchasesAdapter(List<Order> orders) {
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

        holder.button.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), PurchaseDetailActivity.class);
            intent.putExtra("currentEvent", orders.get(position));
            holder.itemView.getContext().startActivity(intent);
        });

        Picasso.get().load(orders.get(position).getEventImageThumb()).into(holder.image);
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

        Button button;
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
            button = itemView.findViewById(R.id.btnMyTickets);
            image = itemView.findViewById(R.id.circleMyPurchasesPicture);
            title = itemView.findViewById(R.id.tvMyPurchasesTitle);
            location = itemView.findViewById(R.id.tvMyPurchasesLocation);
            orderDateTime = itemView.findViewById(R.id.tvMyPurchasesDateTime);
            eventDate = itemView.findViewById(R.id.tvMyPurchasesEventDateTime);
            numberOfTickets = itemView.findViewById(R.id.tvMyPurchasesNumberOfTickets);
            ticketPrice = itemView.findViewById(R.id.tvMyPurchasesTicketPrice);
            totalPurchasePrice = itemView.findViewById(R.id.tvMyPurchasesTotalPrice);
        }
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "EEE MMM dd HH:mm:ss z yyyy";
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
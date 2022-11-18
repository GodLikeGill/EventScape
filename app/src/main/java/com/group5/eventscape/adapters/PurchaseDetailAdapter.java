package com.group5.eventscape.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.group5.eventscape.R;
import com.group5.eventscape.models.Order;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

public class PurchaseDetailAdapter extends RecyclerView.Adapter<PurchaseDetailAdapter.ViewHolder> {

    private Context context;
    private Order event;

    public PurchaseDetailAdapter(Order event) {
        this.event = event;
    }

    @NonNull
    @Override
    public PurchaseDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_layout_purchase_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        generateQR(holder);
        holder.eventDate.setText("Event Date: " + event.getEventDate());
        holder.purchaseDate.setText("Purchase Date: " + event.getOrderDate());
        holder.purchasePrice.setText("Ticket Price: " + event.getTicketPrice());
    }

    @Override
    public int getItemCount() {
        return Integer.parseInt(event.getNumberOfTickets());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView eventDate;
        TextView purchaseDate;
        TextView purchasePrice;
        LinearLayout llPurchaseDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            image = itemView.findViewById(R.id.ivTicketBarcode);
            eventDate = itemView.findViewById(R.id.tvEventDate);
            purchaseDate = itemView.findViewById(R.id.tvPurchaseDate);
            purchasePrice = itemView.findViewById(R.id.tvPurchasePrice);
            llPurchaseDetail = itemView.findViewById(R.id.llPurchaseDetail);
        }
    }

    private void generateQR(@NonNull ViewHolder holder) {
        String eventId = event.getEventId().trim();
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(eventId, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            holder.image.setImageBitmap(bitmap);
        } catch (WriterException e){
            Log.e("TAG", "generateQR: " + e.getLocalizedMessage());
        }
    }
}

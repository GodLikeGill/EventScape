package com.group5.eventscape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.eventscape.R;
import com.group5.eventscape.models.Payment;

import java.util.List;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {

    private Context context;
    private final List<Payment> payments;

    public PaymentsAdapter(List<Payment> payments) {
        this.payments = payments;
    }

    @NonNull
    @Override
    public PaymentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_layout_my_payments, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(payments.get(position).getName());
        holder.number.setText("⬤⬤⬤⬤ ⬤⬤⬤⬤ ⬤⬤⬤⬤  " + payments.get(position).getNumber().substring(payments.get(position).getNumber().length() - 4));
        holder.expiry.setText(payments.get(position).getExpiry());
        holder.type.setImageResource(getCardType(payments.get(position)));
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView number;
        TextView expiry;
        ImageView type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            type = itemView.findViewById(R.id.ivCardType);
            name = itemView.findViewById(R.id.tvCardName);
            number = itemView.findViewById(R.id.tvCardNumber);
            expiry = itemView.findViewById(R.id.tvCardExpiry);
        }
    }

    private int getCardType(Payment payment) {
        if (payment.getType().equals("Visa")) return R.drawable.visa_icon;
        if (payment.getType().equals("MasterCard")) return R.drawable.mastercard_icon;
        if (payment.getType().equals("AmericanExpress")) return R.drawable.amex_icon;
        return 0;
    }
}

package com.group5.eventscape.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.eventscape.R;
import com.group5.eventscape.adapters.PaymentsAdapter;
import com.group5.eventscape.models.Payment;
import com.group5.eventscape.viewmodels.PaymentViewModel;
import com.group5.eventscape.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class PaymentsActivity extends AppCompatActivity {

    TextView balance;
    boolean visaBool = false, amexBool = false, mcBool = false;
    EditText cvv, cardNumber, cardName, cardExpiry;
    Button addPayment;
    ImageButton back;
    ImageView visa, amex, masterCard;
    RecyclerView recyclerView;
    PaymentsAdapter adapter;
    UserViewModel userViewModel;
    PaymentViewModel paymentViewModel;
    List<Payment> payments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        cvv = findViewById(R.id.etCVV);
        visa = findViewById(R.id.ivVisa);
        amex = findViewById(R.id.ivAmex);
        masterCard = findViewById(R.id.ivMasterCard);
        balance = findViewById(R.id.tvBalance);
        back = findViewById(R.id.ibBackPayments);
        cardName = findViewById(R.id.etNameOnCard);
        cardNumber = findViewById(R.id.etCardNumber);
        cardExpiry = findViewById(R.id.etExpiryDate);
        addPayment = findViewById(R.id.btnAddPayment);
        recyclerView = findViewById(R.id.rvPaymentMethods);

        userViewModel = UserViewModel.getInstance(getApplication());
        paymentViewModel = PaymentViewModel.getInstance(getApplication());

        adapter = new PaymentsAdapter(payments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        addPayment.setOnClickListener(v -> {
            if (!cardNumber.getText().toString().isEmpty() && !cardName.getText().toString().isEmpty() && !cardExpiry.getText().toString().isEmpty() && !cvv.getText().toString().isEmpty() && getCardType() != null) {
                Payment newPayment = new Payment();
                newPayment.setName(cardName.getText().toString());
                newPayment.setCvv(cvv.getText().toString());
                newPayment.setNumber(cardNumber.getText().toString());
                newPayment.setExpiry(cardExpiry.getText().toString());
                newPayment.setType(getCardType());
                paymentViewModel.addPayment(newPayment);
                onResume();
            } else {
                Toast.makeText(this, "Please enter all details!", Toast.LENGTH_SHORT).show();
            }
        });

        visa.setOnClickListener(v -> {
            visa.setBackgroundResource(R.drawable.rect_blue_outline);
            amex.setBackgroundResource(R.drawable.shadow_effect_corner);
            masterCard.setBackgroundResource(R.drawable.shadow_effect_corner);
            mcBool = false;
            visaBool = true;
            amexBool = false;
        });

        amex.setOnClickListener(v -> {
            visa.setBackgroundResource(R.drawable.shadow_effect_corner);
            amex.setBackgroundResource(R.drawable.rect_blue_outline);
            masterCard.setBackgroundResource(R.drawable.shadow_effect_corner);
            mcBool = false;
            visaBool = false;
            amexBool = true;
        });

        masterCard.setOnClickListener(v -> {
            visa.setBackgroundResource(R.drawable.shadow_effect_corner);
            amex.setBackgroundResource(R.drawable.shadow_effect_corner);
            masterCard.setBackgroundResource(R.drawable.rect_blue_outline);
            mcBool = true;
            visaBool = false;
            amexBool = false;
        });

        back.setOnClickListener(v -> finish());
    }

    private String getCardType() {
        if (visaBool) return "Visa";
        if (mcBool) return "MasterCard";
        if (amexBool) return "AmericanExpress";
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        paymentViewModel.getPayments();
        paymentViewModel.allPayments.observe(this, allPayments -> {
            payments.clear();
            payments.addAll(allPayments);
            adapter.notifyDataSetChanged();
        });

        userViewModel.getCurrentUser(FirebaseAuth.getInstance().getUid());
        userViewModel.currentUser.observe(this, user -> {
            balance.setText("$" + user.getBalance());
        });
    }
}
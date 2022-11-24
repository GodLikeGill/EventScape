package com.group5.eventscape.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.group5.eventscape.R;
import com.group5.eventscape.adapters.PurchaseDetailAdapter;
import com.group5.eventscape.models.Order;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PurchaseSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = this.getClass().getCanonicalName();

    private String curOrderId;
    private Order curOrder;

    private TextView purchaseID;
    private TextView eventTitle;
    private TextView eventDate;
    private TextView eventTime;
    private TextView eventLocation;
    private TextView ticketPrice;
    private TextView numberOfTicket;
    private TextView amountPaid;
    private Button goToHome;

    private ImageView barcodeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_summary);

        // Toolbar code
        Toolbar toolbar = findViewById(R.id.toolbarPurchaseSummary);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        //get purchase data
        this.curOrderId = (String) getIntent().getStringExtra("curOrderId");
        this.curOrder = (Order) getIntent().getParcelableExtra("curOrder");

        this.purchaseID = findViewById(R.id.tvPurchaseId);
        this.purchaseID.setText(this.curOrderId);

        this.eventTitle = findViewById(R.id.tvEventTitle);
        this.eventTitle.setText(this.curOrder.getEventTitle());

        this.eventDate = findViewById(R.id.tvEventDate);
        this.convertDate(this.curOrder.getEventDate());

        this.eventTime = findViewById(R.id.tvEventTime);
        this.eventTime.setText(this.curOrder.getEventTime());

        this.eventLocation = findViewById(R.id.tvEventLocation);
        this.eventLocation.setText(this.curOrder.getEventLocation());

        this.ticketPrice = findViewById(R.id.tvTicketPrice);
        this.ticketPrice.setText("CA $" + String.format("%.2f", Double.parseDouble(this.curOrder.getTicketPrice()) ));

        this.numberOfTicket = findViewById(R.id.tvNumberOfTicket);
        this.numberOfTicket.setText(this.curOrder.getNumberOfTickets());

        this.amountPaid = findViewById(R.id.tvAmountPaid);
        this.amountPaid.setText( "CA $" + this.curOrder.getTotalOrderPrice());

        this.goToHome = findViewById(R.id.btnGoToHome);
        this.goToHome.setOnClickListener(this);

        barcodeImage = findViewById(R.id.ivPurchaseSummaryBarcode);
        generateQR();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.goToHomeScreen();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void convertDate(String eventDate) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = eventDate;
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, MMM dd");
        try {
            Date date = inputDateFormat.parse(dateInString);
            this.eventDate.setText(outputDateFormat.format(date));
        } catch (ParseException e) {
            Log.e(TAG, "Date Error");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view != null){
            switch (view.getId()){
                case R.id.btnGoToHome:{
                    this.goToHomeScreen();
                    break;
                }
            }
        }
    }

    private void goToHomeScreen() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        finish();

//        Intent myIntent = new Intent(this, MainActivity.class);
//        startActivity(myIntent);

//        finish();
    }

    private void generateQR() {
        String eventId = curOrder.getEventId().trim();
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(eventId, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            barcodeImage.setImageBitmap(bitmap);
        } catch (WriterException e){
            Log.e("TAG", "generateQR: " + e.getLocalizedMessage());
        }
    }
}
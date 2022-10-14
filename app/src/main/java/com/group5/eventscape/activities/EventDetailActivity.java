package com.group5.eventscape.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.group5.eventscape.R;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.models.Orders;
import com.group5.eventscape.viewmodels.OrdersViewModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private String TAG = this.getClass().getCanonicalName();
    //firebase
    private FirebaseAuth mAuth;

    private Spinner spinner;
    private static final Integer[] paths = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private Event curEvent;
    private String purchaseTotal;
    private Integer numberOfTickets;

    private String loggedInUserId;
    private String loggedInUserEmail;

    private ImageView eventImage;
    private TextView eventTitle;
    private TextView eventDate;
    private TextView eventTime;
    private TextView eventAddreess1;
    private TextView eventAddreess2;
    private TextView eventPrice;
    private TextView eventDescription;
    private Button purchaseButton;

    private Orders order;
    private OrdersViewModel ordersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        this.loggedInUserId = currentUser.getUid();
        this.loggedInUserEmail = currentUser.getEmail();

        //Spinner Code
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(EventDetailActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //get event data
        this.curEvent = (Event) getIntent().getParcelableExtra("curEvent");
        this.purchaseTotal = this.curEvent.getPrice();


        // Toolbar code
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Set data in fields
        this.eventImage = findViewById(R.id.bannerImage);
        Picasso.get().load(this.curEvent.getImage()).into(this.eventImage );

        this.eventTitle = findViewById(R.id.eventTitle);
        this.eventTitle.setText(this.curEvent.getTitle());

        this.eventDate = findViewById(R.id.eventDate);
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = this.curEvent.getDate();
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, MMM dd");
        try {
            Date date = inputDateFormat.parse(dateInString);
            this.eventDate.setText(outputDateFormat.format(date));
        } catch (ParseException e) {
            Log.e(TAG, "Date Error");
            e.printStackTrace();
        }

        this.eventTime = findViewById(R.id.eventTime);
        this.eventTime.setText(this.curEvent.getTime());

        this.eventAddreess1 = findViewById(R.id.address1);
        this.eventAddreess1.setText(this.curEvent.getAddress());

        this.eventAddreess2 = findViewById(R.id.address2);
        this.eventAddreess2.setText(this.curEvent.getCity() + ", " + this.curEvent.getProvince() + ", " + this.curEvent.getPostCode() );

        this.eventPrice = findViewById(R.id.eventPrice);
        this.eventPrice.setText("CA $" + this.curEvent.getPrice());

        this.eventDescription = findViewById(R.id.eventDescription);
        this.eventDescription.setText(this.curEvent.getDesc());

        this.purchaseButton = findViewById(R.id.btnBuyNow);
        this.purchaseButton.setText("PURCHASE FOR CA $" + this.purchaseTotal);

        this.purchaseButton = findViewById(R.id.btnBuyNow);
        this.purchaseButton.setOnClickListener(this);


        // place order
        this.order = new Orders();
        this.ordersViewModel = OrdersViewModel.getInstance(this.getApplication());

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        this.numberOfTickets = Integer.parseInt(this.spinner.getSelectedItem().toString());
        double totalPrice = getFinalPrice(this.numberOfTickets);
        this.purchaseTotal = String.format("%.2f", totalPrice);
        this.purchaseButton.setText("PURCHASE FOR CA $" + this.purchaseTotal);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionFavorite:
                Toast.makeText(EventDetailActivity.this, "Added to Favorites", Toast.LENGTH_LONG).show();
                return true;

            case android.R.id.home:
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public double getFinalPrice(Integer numberOfTickets) {
        double eventPrice = Double.parseDouble(curEvent.getPrice());
        return eventPrice * numberOfTickets;
    }

    @Override
    public void onClick(View view) {
        if (view != null){
            switch (view.getId()){
                case R.id.btnBuyNow:{
                    Log.d(TAG, "onClick: Buy now Clicked");
                    this.placeOrder();
                    break;
                }
            }
        }
    }

    private void placeOrder(){
        Date currentTime = Calendar.getInstance().getTime();


        this.order.setEventId(curEvent.getId());
        this.order.setUserId(this.loggedInUserId);
        this.order.setUserEmail(this.loggedInUserEmail);
        this.order.setNumberOfTickets(this.numberOfTickets.toString());
        this.order.setTicketPrice(curEvent.getPrice());
        this.order.setTotalOrderPrice(this.purchaseTotal);
        this.order.setOrderDate(currentTime.toString());

        this.ordersViewModel.addOrder(this.order);
        Toast.makeText(EventDetailActivity.this, "Order successfully placed", Toast.LENGTH_SHORT).show();



        //showSuccess(); // show success and goto main screen
    }

//    private void showSuccess() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
//
//        builder.setTitle("Success");
//        builder.setMessage("Order placed successfully!");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                goToHomeScreen();
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//
//    private void goToHomeScreen() {
//        this.finish();
//    }
}
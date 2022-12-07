package com.group5.eventscape.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.eventscape.R;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.models.Favorite;
import com.group5.eventscape.models.Order;
import com.group5.eventscape.models.User;
import com.group5.eventscape.viewmodels.FavoriteViewModel;
import com.group5.eventscape.viewmodels.OrderViewModel;
import com.group5.eventscape.viewmodels.UserViewModel;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private String TAG = this.getClass().getCanonicalName();

    //Toolbar menu
    private Menu toolbarMenu;

    public Boolean isFavoriteEvent = false;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Spinner spinner;
    private static final Integer[] paths = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private Event curEvent;
    private String purchaseTotal;
    private Integer numberOfTickets = 1;

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

    private Button minusButton;
    private Button plusButton;
    private EditText totalTickets;

    private Order order;
    private OrderViewModel ordersViewModel;

    private Favorite favorite;
    private FavoriteViewModel favoriteViewModel;

    private String generatedOrderId;

    //user
    UserViewModel userViewModel;
    private User currentUser;
    String currentUserEmail;
    String currUserBalance;

    private User eventOwnerUser;
    String eventOwnerEmail;
    String eventOwnerBalance;

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
//        spinner = (Spinner)findViewById(R.id.spinner);
//        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(EventDetailActivity.this,
//                android.R.layout.simple_spinner_item,paths);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);

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
        String fromDateInString = this.curEvent.getDate();
        String toDateInString = "";
        if(this.curEvent.getDate2() != null){
            toDateInString = this.curEvent.getDate2();
        }

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, MMM dd");
        try {
            Date fromDate = inputDateFormat.parse(fromDateInString);
            if(!toDateInString.isEmpty()){
                toDateInString = " to " + outputDateFormat.format(inputDateFormat.parse(toDateInString));

            }
            this.eventDate.setText(outputDateFormat.format(fromDate) + toDateInString);
        } catch (ParseException e) {
            Log.e(TAG, "Date Error");
            e.printStackTrace();
        }

        this.eventTime = findViewById(R.id.eventTime);
        this.eventTime.setText(convertTime(this.curEvent.getTime()));

        this.eventAddreess1 = findViewById(R.id.address1);
        this.eventAddreess1.setText(this.curEvent.getAddress());

        this.eventAddreess2 = findViewById(R.id.address2);
        this.eventAddreess2.setText(this.curEvent.getCity() + ", " + this.curEvent.getProvince() + ", " + this.curEvent.getPostCode() );

        this.eventPrice = findViewById(R.id.eventPrice);
        this.eventPrice.setText("CA $" + this.curEvent.getPrice());

        this.eventDescription = findViewById(R.id.eventDescription);
        this.eventDescription.setText((!this.curEvent.getCategory().isEmpty() ? "Category: " + this.curEvent.getCategory() + "\n\n" : "" ) + this.curEvent.getDesc());

        this.purchaseButton = findViewById(R.id.btnBuyNow);
        this.purchaseButton.setText("PURCHASE FOR CA $" + this.purchaseTotal);

//        this.purchaseButton = findViewById(R.id.btnBuyNow);
        this.purchaseButton.setOnClickListener(this);

        this.minusButton = findViewById(R.id.btnMinus);
        this.minusButton.setOnClickListener(this);

        this.plusButton = findViewById(R.id.btnPlus);
        this.plusButton.setOnClickListener(this);

        this.totalTickets = findViewById(R.id.etNumberOfTickets);


        // place order
        this.order = new Order();
        this.ordersViewModel = OrderViewModel.getInstance(this.getApplication());

        // add to favorite
        this.favorite = new Favorite();
        this.favoriteViewModel = FavoriteViewModel.getInstance(this.getApplication());


        this.favoriteViewModel.getFavoriteForCurrentEvent(curEvent.getId());
        this.favoriteViewModel.favoriteEvent.observe(this, fav -> {
            favorite = fav;
            Log.e(TAG, "NNNK: " + fav.getEventId());
            this.isFavoriteEvent = true;
        });


//        this.dateToMill();

        this.checkFavorite();

        // user
        userViewModel = UserViewModel.getInstance(getApplication());
    }

    /*
    private void dateToMill() {
        String myDate = "25/11/2024";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();

        Log.e(TAG, "checkDate: " + myDate + " in milliseconds: " + millis);



        this.millToDate(millis);
    }

    private void millToDate(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(millis));
        Log.e(TAG, "millToDate: " + millis + " in date: " + dateString);


    }
    */


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
        this.toolbarMenu = menu;

        if(isFavoriteEvent){
            this.toolbarMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_filled_24));
        }
        else{
            this.toolbarMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_24));
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionFavorite:
                this.addToFavorite();
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
                    this.confirmPurchase();
                    break;
                }
                case R.id.btnMinus:{
                    this.minusNumberOfTickets();
                    break;
                }
                case R.id.btnPlus:{
                    this.plusNumberOfTickets();
                    break;
                }
            }
        }
    }

    private void plusNumberOfTickets() {
        Log.d(TAG, "onClick: Plus Clicked");
        Integer tickets = Integer.parseInt(this.totalTickets.getText().toString());
        if(tickets < 10){
            tickets++;
            this.totalTickets.setText(tickets.toString());
        }
        this.updatePurchase(tickets);

    }

    private void minusNumberOfTickets() {
        Log.d(TAG, "onClick: Minus Clicked");
        Integer tickets = Integer.parseInt(this.totalTickets.getText().toString());
        if(tickets > 1){
            tickets--;
            this.totalTickets.setText(tickets.toString());
        }
        this.updatePurchase(tickets);
    }

    private void updatePurchase(Integer tickets) {
        this.numberOfTickets = tickets;
        double totalPrice = getFinalPrice(this.numberOfTickets);
        this.purchaseTotal = String.format("%.2f", totalPrice);
        this.purchaseButton.setText("PURCHASE FOR CA $" + this.purchaseTotal);
    }

    private void addToFavorite(){
        toggleFavorite();
        this.favorite.setEventId(curEvent.getId());
        this.favoriteViewModel.checkForFavorite(this.favorite);
    }

    private void toggleFavorite() {
        Log.e(TAG, "toggleFavorite: " + isFavoriteEvent);
        if(isFavoriteEvent){
            isFavoriteEvent = false;
            supportInvalidateOptionsMenu();
        }
        else{
            isFavoriteEvent = true;
            supportInvalidateOptionsMenu();
        }
    }

    private void checkFavorite() {
        this.favoriteViewModel.isFavoriteAtLoad.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    isFavoriteEvent = true;
                    supportInvalidateOptionsMenu();
                }
                else{
                    isFavoriteEvent = false;
                    supportInvalidateOptionsMenu();
                }
                Log.e(TAG, "onOptionsItemSelected: " + isFavoriteEvent);
            }
        });
    }

    private void confirmPurchase(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailActivity.this);

        builder.setMessage("Would you like to proceed with this purchase?");
        builder.setTitle("Confirm Purchase!");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            this.placeOrder();
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void placeOrder(){
        Log.e(TAG, "placeOrder: " + currentUser.getEmail() );
        Log.e(TAG, "placeOrder: " + eventOwnerUser.getEmail() );
        if(!currentUser.getId().equals(eventOwnerUser.getId())){
            Log.e(TAG, "placeOrder: inside condition" );
            Log.e(TAG, "placeOrder user: " + this.currUserBalance );
            Log.e(TAG, "placeOrder eventOwner: " + this.eventOwnerBalance );

            Double updatedCurrentUserBalance = Double.parseDouble(this.currUserBalance) + Double.parseDouble(this.purchaseTotal);
            Double updatedEventOwnerBalance = Double.parseDouble(this.eventOwnerBalance) - Double.parseDouble(this.purchaseTotal);

            currentUser.setBalance(updatedCurrentUserBalance.toString());
            userViewModel.updateUserBalance(currentUser);
            eventOwnerUser.setBalance(updatedEventOwnerBalance.toString());
            userViewModel.updateUserBalance(eventOwnerUser);

            Log.e(TAG, "placeOrder user: " + updatedCurrentUserBalance );
            Log.e(TAG, "placeOrder eventOwner: " + updatedEventOwnerBalance );
        }

        Date currentTime = Calendar.getInstance().getTime();
        this.order.setEventId(curEvent.getId());
        this.order.setEventImageThumb(curEvent.getImage());
        this.order.setEventTitle(curEvent.getTitle());
        this.order.setEventLocation(curEvent.getAddress() + ", " + curEvent.getCity() + ", " + curEvent.getProvince() + ", " + curEvent.getPostCode());
        this.order.setEventDate(curEvent.getDate());
        this.order.setEventTime(curEvent.getTime());

        this.order.setUserId(this.loggedInUserId);
        this.order.setUserEmail(this.loggedInUserEmail);
        this.order.setNumberOfTickets(this.numberOfTickets.toString());
        this.order.setTicketPrice(curEvent.getPrice());
        this.order.setTotalOrderPrice(this.purchaseTotal);
        this.order.setOrderDate(currentTime.toString());

        this.ordersViewModel.addOrder(this.order);
        this.ordersViewModel.generatedOrderId.observe(this, id -> {
            if(this.generatedOrderId != id){
                Intent intent = new Intent(this, PurchaseSummaryActivity.class);
                intent.putExtra("curOrderId", id);
                intent.putExtra("curOrder", this.order);
                startActivity(intent);
                this.generatedOrderId = id;
                Log.e(TAG, "placeOrder: " + this.generatedOrderId + " " + id);
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();

        //user Balance
        userViewModel.getCurrentUser(FirebaseAuth.getInstance().getUid());
        userViewModel.currentUser.observe(this, user -> {
            this.currentUser = user;
            this.currUserBalance = user.getBalance();
            this.currentUserEmail = user.getEmail();
            Log.e(TAG, "currUserBalance nkbalance: " + currUserBalance);
            Log.e(TAG, "currUserBalance nkbalance: " + currentUserEmail);
        });

        userViewModel.getUserByEmail(curEvent.getUser());
        userViewModel.userByEmail.observe(this, user -> {
            this.eventOwnerUser = user;
            this.eventOwnerBalance = user.getBalance();
            this.eventOwnerEmail = user.getEmail();
            Log.e(TAG, "eventOwnerBalance nkbalance: " + eventOwnerBalance);
            Log.e(TAG, "eventOwnerBalance nkbalance: " + eventOwnerEmail);
        });


    }
}
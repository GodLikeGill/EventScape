package com.group5.eventscape.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.group5.eventscape.models.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {
    private final FirebaseFirestore db;
    private final String COLLECTION_ORDERS = "Orders";
    private final String FIELD_ID = "id";
    private final String FIELD_EVENT_ID = "eventId";

    private final String FIELD_EVENT_IMAGETHUMB = "eventImageThumb";
    private final String FIELD_EVENT_TITLE = "eventTitle";
    private final String FIELD_EVENT_LOCATION = "eventLocation";
    private final String FIELD_EVENT_DATE = "eventDate";
    private final String FIELD_EVENT_TIME = "eventTime";

    private final String FIELD_USER_ID = "userId";
    private final String FIELD_USER_EMAIL = "userEmail";
    private final String FIELD_NUMBER_OF_TICKETS = "numberOfTickets";
    private final String FIELD_TICKET_PRICE = "ticketPrice";
    private final String FIELD_TOTAL_ORDER_PRICE = "totalOrderPrice";
    private final String FIELD_ORDER_DATE = "orderDate";
    private final String FIELD_ORDER_DATE_TIMESTAMP = "orderDateTimeStamp";

    public MutableLiveData<List<Order>> allOrders = new MutableLiveData<>();
    public MutableLiveData<List<Order>> userOrders = new MutableLiveData<>();
    public MutableLiveData<String> generatedOrderId = new MutableLiveData<>();

    public OrderRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllOrders() {
        try {
            db.collection(COLLECTION_ORDERS).get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<Order> ordersList = new ArrayList<>();
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("TAG", "getAllOrders: No data retrieved.");
                } else {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        Order orders = documentChange.getDocument().toObject(Order.class);
                        ordersList.add(orders);
                        Log.d("TAG", "getAllOrders: " + orders.getId());
                    }
                }
                allOrders.postValue(ordersList);
            });
        } catch (Exception e) {
            Log.e("TAG", "getAllOrders: " + e.getLocalizedMessage());
        }
    }

    public void getOrdersOfCurUser(String userEmail) {
        Log.e("TAG", "getOrdersOfCurUser: start" );
        try {
            Log.e("TAG", "getOrdersOfCurUser: try" );
            db.collection(COLLECTION_ORDERS)
                    .whereEqualTo("userEmail", userEmail)
                    //.orderBy("orderDateTimeStamp", Query.Direction.DESCENDING)
                    //.orderBy("eventTitle", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Order> ordersList = new ArrayList<>();
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "getOrdersOfCurUser: No data retrieved.");
                        } else {
                            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                Order orders = documentChange.getDocument().toObject(Order.class);
                                ordersList.add(orders);
                                Log.d("TAG", "getOrdersOfCurUser: " + orders.getEventId());
                            }
                        }
                        userOrders.postValue(ordersList);
                    });
        } catch (Exception e) {
            Log.e("TAG", "getOrdersOfCurUser: catch" );
            Log.e("TAG", "getOrdersOfCurUser: " + e.getLocalizedMessage());
        }
        Log.e("TAG", "getOrdersOfCurUser: end" );
    }

    public void addOrder(Order order) {
        try {
            Map<String, Object> data = new HashMap<>();
//            data.put(FIELD_ID, order.getId());
            data.put(FIELD_EVENT_ID, order.getEventId());

            data.put(FIELD_EVENT_IMAGETHUMB, order.getEventImageThumb());
            data.put(FIELD_EVENT_TITLE, order.getEventTitle());
            data.put(FIELD_EVENT_LOCATION, order.getEventLocation());
            data.put(FIELD_EVENT_DATE, order.getEventDate());
            data.put(FIELD_EVENT_TIME, order.getEventTime());

            data.put(FIELD_USER_ID, order.getUserId());
            data.put(FIELD_USER_EMAIL, order.getUserEmail());
            data.put(FIELD_NUMBER_OF_TICKETS, order.getNumberOfTickets());
            data.put(FIELD_TICKET_PRICE, order.getTicketPrice());
            data.put(FIELD_TOTAL_ORDER_PRICE, order.getTotalOrderPrice());
            data.put(FIELD_ORDER_DATE, order.getOrderDate());
            data.put(FIELD_ORDER_DATE_TIMESTAMP, FieldValue.serverTimestamp());

            db.collection(COLLECTION_ORDERS).add(data).addOnSuccessListener(documentReference -> {
                Log.d("TAG", "addOrder: Order created successfully with order id: " + documentReference.getId());
                generatedOrderId.postValue(documentReference.getId());
            }).addOnFailureListener(e -> {
                Log.e("TAG", "onFailure: Error while creating orders " + e.getLocalizedMessage());
            });
        } catch (Exception e) {
            Log.e("TAG", "addOrder: " + e.getLocalizedMessage());
        }
    }
}

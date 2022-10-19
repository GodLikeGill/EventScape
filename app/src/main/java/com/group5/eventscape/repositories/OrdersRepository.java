package com.group5.eventscape.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.models.Favorite;
import com.group5.eventscape.models.Orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersRepository {
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

    public MutableLiveData<List<Orders>> allOrders = new MutableLiveData<>();
    public MutableLiveData<List<Orders>> userOrders = new MutableLiveData<>();

    public OrdersRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllOrders() {
        try {
            db.collection(COLLECTION_ORDERS).get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<Orders> ordersList = new ArrayList<>();
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("TAG", "getAllOrders: No data retrieved.");
                } else {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        Orders orders = documentChange.getDocument().toObject(Orders.class);
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
        try {
            db.collection(COLLECTION_ORDERS)
                    .whereEqualTo("userEmail", userEmail)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Orders> ordersList = new ArrayList<>();
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "getUserOrders: No data retrieved.");
                        } else {
                            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                Orders orders = documentChange.getDocument().toObject(Orders.class);
                                ordersList.add(orders);
                                Log.d("TAG", "getUserOrders: " + orders.getId());
                            }
                        }
                        userOrders.postValue(ordersList);
            });
        } catch (Exception e) {
            Log.e("TAG", "getAllOrders: " + e.getLocalizedMessage());
        }
    }

    public void addOrder(Orders order) {
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

            db.collection(COLLECTION_ORDERS).add(data).addOnSuccessListener(documentReference -> {
                Log.d("TAG", "addOrder: Order created successfully");
            }).addOnFailureListener(e -> {
                Log.e("TAG", "onFailure: Error while creating orders " + e.getLocalizedMessage());
            });
        } catch (Exception e) {
            Log.e("TAG", "addOrder: " + e.getLocalizedMessage());
        }
    }
}

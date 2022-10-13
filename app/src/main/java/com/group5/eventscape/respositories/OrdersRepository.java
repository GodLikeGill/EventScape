package com.group5.eventscape.respositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.eventscape.models.Orders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersRepository {
    private final FirebaseFirestore db;
    private final String COLLECTION_ORDERS = "Orders";
    private final String FIELD_ID = "id";
    private final String FIELD_EVENT_ID = "eventId";
    private final String FIELD_USER_ID = "userId";
    private final String FIELD_USER_EMAIL = "userEmail";
    private final String FIELD_NUMBER_OF_TICKETS = "numberOfTickets";
    private final String FIELD_TICKET_PRICE = "ticketPrice";
    private final String FIELD_TOTAL_ORDER_PRICE = "totalOrderPrice";
    private final String FIELD_ORDER_DATE = "orderDate";

    public MutableLiveData<List<Orders>> allOrders = new MutableLiveData<>();

    public OrdersRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void addOrder(Orders order) {
        try {
            Map<String, Object> data = new HashMap<>();
//            data.put(FIELD_ID, order.getId());
            data.put(FIELD_EVENT_ID, order.getEventId());
            data.put(FIELD_USER_ID, order.getUserId());
            data.put(FIELD_USER_EMAIL, order.getUserEmail());
            data.put(FIELD_NUMBER_OF_TICKETS, order.getNumberOfTickets());
            data.put(FIELD_TICKET_PRICE, order.getTicketPrice());
            data.put(FIELD_TOTAL_ORDER_PRICE, order.getTotalOrderPrice());
            data.put(FIELD_ORDER_DATE, order.getOrderDate());

//            DB.collection(COLLECTION_USERS)
//                    .document(loggedInUserEmail)
//                    .collection(COLLECTION_FRIENDS)
//                    .add(data)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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

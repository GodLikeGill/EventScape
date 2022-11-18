package com.group5.eventscape.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.eventscape.models.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentRepository {

    private final FirebaseFirestore db;
    private final String FIELD_NUMBER = "number";
    private final String FIELD_NAME = "name";
    private final String FIELD_TYPE = "type";
    private final String FIELD_CVV = "cvv";
    private final String FIELD_EXPIRY = "expiry";
    private final String COLLECTION_USERS = "Users";
    private final String COLLECTION_PAYMENTS = "payments";
    public String loggedInUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public PaymentRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<List<Payment>> allPayments = new MutableLiveData<>();

    public void addPayment(Payment payment) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_NAME, payment.getName());
            data.put(FIELD_NUMBER, payment.getNumber());
            data.put(FIELD_CVV, payment.getCvv());
            data.put(FIELD_EXPIRY, payment.getExpiry());
            data.put(FIELD_TYPE, payment.getType());
            db.collection(COLLECTION_USERS)
                    .document(loggedInUserId)
                    .collection(COLLECTION_PAYMENTS)
                    .document().set(data).addOnSuccessListener(unused -> Log.d("TAG", "onSuccess: Payment added successfully"))
                    .addOnFailureListener(e -> Log.e("TAG", "addPayment: " + e.getLocalizedMessage()));
        } catch (Exception e) {
            Log.e("TAG", "addPayment: " + e.getLocalizedMessage());
        }
    }

    public void getPayments(){
        try {
            db.collection(COLLECTION_USERS).document(loggedInUserId).collection(COLLECTION_PAYMENTS).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Payment> paymentList = new ArrayList<>();
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.e("TAG", "getPayments: No data retrieved");
                        } else {
                            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                Payment payment = documentChange.getDocument().toObject(Payment.class);
                                paymentList.add(payment);
                                Log.d("TAG", "getPayments: " + payment.getNumber());
                            }
                        }
                        allPayments.postValue(paymentList);
                        Log.d("TAG", "onSuccess: Successfully retrieved payments.");
                    }).addOnFailureListener(e -> Log.e("TAG", "onFailure: Error while retrieving payments" ));
        } catch (Exception e){
            Log.e("TAG", "getPayments: " + e.getLocalizedMessage());
        }
    }
}

package com.group5.eventscape.respositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.eventscape.models.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRepository {

    private final FirebaseFirestore db;
    private final String COLLECTION_EVENTS = "Events";
    private final String FIELD_ID = "id";
    private final String FIELD_USER = "user";
    private final String FIELD_TITLE = "title";
    private final String FIELD_CATEGORY = "category";
    private final String FIELD_DESC = "desc";
    private final String FIELD_ADDRESS = "address";
    private final String FIELD_CITY = "city";
    private final String FIELD_PROVINCE = "province";
    private final String FIELD_POSTCODE = "postcode";
    private final String FIELD_DATE = "date";
    private final String FIELD_TIME = "time";
    private final String FIELD_PRICE = "price";
    private final String FIELD_IMAGE = "image";

    public MutableLiveData<List<Event>> allEvents = new MutableLiveData<>();

    public EventRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllEvents() {
        try {
            db.collection(COLLECTION_EVENTS).get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<Event> eventsList = new ArrayList<>();
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("TAG", "getAllListings: No data retrieved.");
                } else {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        Event event = documentChange.getDocument().toObject(Event.class);
                        eventsList.add(event);
                        Log.d("TAG", "getAllListings: " + event.getAddress());
                    }
                }
                allEvents.postValue(eventsList);
            });
        } catch (Exception e) {
            Log.e("TAG", "getAllListings: " + e.getLocalizedMessage());
        }
    }

    public void addEvent(Event event) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_ID, event.getId());
            data.put(FIELD_USER, event.getUser());
            data.put(FIELD_TITLE, event.getTitle());
            data.put(FIELD_CATEGORY, event.getCategory());
            data.put(FIELD_DESC, event.getDesc());
            data.put(FIELD_ADDRESS, event.getAddress());
            data.put(FIELD_CITY, event.getCity());
            data.put(FIELD_PROVINCE, event.getProvince());
            data.put(FIELD_POSTCODE, event.getPostCode());
            data.put(FIELD_DATE, event.getDate());
            data.put(FIELD_TIME, event.getTime());
            data.put(FIELD_PRICE, event.getPrice());
            data.put(FIELD_IMAGE, event.getImage());
            db.collection(COLLECTION_EVENTS).document(event.getId()).set(data).addOnSuccessListener(documentReference -> {
                Log.d("TAG", "addListing: Listing created successfully");
            }).addOnFailureListener(e -> {
                Log.e("TAG", "onFailure: Error while creating document " + e.getLocalizedMessage());
            });
        } catch (Exception e) {
            Log.e("TAG", "addListing: " + e.getLocalizedMessage());
        }
    }

    public void deleteEvent(String id) {
        try {
            db.collection(COLLECTION_EVENTS).document(id).delete()
                    .addOnSuccessListener(unused -> Log.d("TAG", "deleteListing: Successfully deleted listing"))
                    .addOnFailureListener(e -> Log.e("TAG", "deleteListing: " + e.getLocalizedMessage()));
        } catch (Exception e) {
            Log.e("TAG", "deleteListing: " + e.getLocalizedMessage());
        }
    }
}


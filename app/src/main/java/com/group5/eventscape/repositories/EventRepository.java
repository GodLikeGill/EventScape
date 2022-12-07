package com.group5.eventscape.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.models.Favorite;

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
    private final String FIELD_LONGITUDE = "longitude";
    private final String FIELD_LATITUDE = "latitude";
    private final String FIELD_CITY = "city";
    private final String FIELD_PROVINCE = "province";
    private final String FIELD_POSTCODE = "postcode";
    private final String FIELD_DATE_IN_MILLI = "startDateInMilli";
    private final String FIELD_DATE = "date";
    private final String FIELD_DATE2 = "date2";
    private final String FIELD_TIME = "time";
    private final String FIELD_PRICE = "price";
    private final String FIELD_IMAGE = "image";

    public MutableLiveData<List<Event>> allEvents = new MutableLiveData<>();
    public MutableLiveData<Event> eventById = new MutableLiveData<>();

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

    public void getEventById(String id) {
        try {
            db.collection(COLLECTION_EVENTS)
                    .document(id)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            eventById.postValue(documentSnapshot.toObject(Event.class));
                        } else {
                            Log.e("TAG", "getEvent: No data retrieved.");
                        }
                    }).addOnFailureListener(e -> {
                        Log.e("TAG", "getEvent: " + e.getLocalizedMessage());
                    });
        } catch (Exception e) {
            Log.e("TAG", "getEvent: " + e.getLocalizedMessage());
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
            data.put(FIELD_LONGITUDE, event.getLongitude());
            data.put(FIELD_LATITUDE, event.getLatitude());
            data.put(FIELD_PROVINCE, event.getProvince());
            data.put(FIELD_POSTCODE, event.getPostCode());
            data.put(FIELD_DATE, event.getDate());
            data.put(FIELD_DATE2, event.getDate2());
            data.put(FIELD_TIME, event.getTime());
            data.put(FIELD_PRICE, event.getPrice());
            data.put(FIELD_IMAGE, event.getImage());
            data.put(FIELD_DATE_IN_MILLI, event.getStartDateInMilli());
            db.collection(COLLECTION_EVENTS).document(event.getId()).set(data).addOnSuccessListener(documentReference -> {
                Log.d("TAG", "addListing: Listing created successfully");
            }).addOnFailureListener(e -> {
                Log.e("TAG", "onFailure: Error while creating document " + e.getLocalizedMessage());
            });
        } catch (Exception e) {
            Log.e("TAG", "addListing: " + e.getLocalizedMessage());
        }
    }

    public void editEvent(Event event) {
        try {
            Map<String, Object> data2 = new HashMap<>();
            data2.put(FIELD_ID, event.getId());
            data2.put(FIELD_USER, event.getUser());
            data2.put(FIELD_TITLE, event.getTitle());
            data2.put(FIELD_CATEGORY, event.getCategory());
            data2.put(FIELD_DESC, event.getDesc());

            data2.put(FIELD_ADDRESS, event.getAddress());
            data2.put(FIELD_CITY, event.getCity());
            data2.put(FIELD_LONGITUDE, event.getLongitude());
            data2.put(FIELD_LATITUDE, event.getLatitude());
            data2.put(FIELD_PROVINCE, event.getProvince());
            data2.put(FIELD_POSTCODE, event.getPostCode());
            data2.put(FIELD_DATE, event.getDate());
            data2.put(FIELD_DATE2, event.getDate2());
            data2.put(FIELD_TIME, event.getTime());
            data2.put(FIELD_PRICE, event.getPrice());
            data2.put(FIELD_IMAGE, event.getImage());
            data2.put(FIELD_DATE_IN_MILLI, event.getStartDateInMilli());
            db.collection(COLLECTION_EVENTS).document(event.getId()).update(data2).addOnSuccessListener(documentReference -> {
                Log.d("TAG", "EditListing: Listing created successfully");
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


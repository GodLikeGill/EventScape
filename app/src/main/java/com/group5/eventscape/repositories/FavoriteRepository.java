package com.group5.eventscape.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.models.Favorite;
import com.group5.eventscape.models.User;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteRepository {
    private final FirebaseFirestore db;
    private final String COLLECTION_USERS = "Users";
    private final String COLLECTION_FAVORITE = "favorite";
    private final String FIELD_ID = "id";
    private final String FIELD_EVENT_ID = "eventId";

    //String currUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public String loggedInUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public MutableLiveData<Favorite> favoriteEvent = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    public MutableLiveData<List<Favorite>> allFav = new MutableLiveData<>();

    public FavoriteRepository() {
        db = FirebaseFirestore.getInstance();
    }



    public void addToFavorite(Favorite fav) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_EVENT_ID, fav.getEventId());

            db.collection(COLLECTION_USERS)
                    .document(loggedInUserId)
                    .collection(COLLECTION_FAVORITE)
                    .document(fav.getEventId()).set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.e("TAG", "onSuccess: Event Added to favorite successfully created");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "onFailure: Error while adding to favorite" + e.getLocalizedMessage() );
                        }
                    });
        } catch (Exception e) {
            Log.e("TAG", "addToFavorite Exception: " + e.getLocalizedMessage());
        }
    }

    public void getAllFav() {
        try {
            db.collection(COLLECTION_USERS)
                    .document(loggedInUserId)
                    .collection(COLLECTION_FAVORITE).get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<Favorite> favoriteList = new ArrayList<>();
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("TAG", "getAllListings: No data retrieved.");
                } else {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        Favorite favorite = documentChange.getDocument().toObject(Favorite.class);
                        favoriteList.add(favorite);
                        Log.d("TAG", "getAllListings FAv: " + favorite.getEventId());
                    }
                }
                        allFav.postValue(favoriteList);
            });
        } catch (Exception e) {
            Log.e("TAG", "getAllListings: " + e.getLocalizedMessage());
        }
    }


    public void getFavoriteForCurrentEvent(String id) {
        try {
            db.collection(COLLECTION_USERS)
                    .document(loggedInUserId)
                    .collection(COLLECTION_FAVORITE)
                    .document(id)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            favoriteEvent.postValue(documentSnapshot.toObject(Favorite.class));

                        } else {
                            Log.e("TAG", "getFavorite: No data retrieved.");
                        }
                    }).addOnFailureListener(e -> {
                        Log.e("TAG", "getFavorite: " + e.getLocalizedMessage());
                });
        } catch (Exception e) {
            Log.e("TAG", "getFavorite: " + e.getLocalizedMessage());
        }
    }

    public void checkForFavorite(Favorite fav) {
        db.collection(COLLECTION_USERS)
                .document(loggedInUserId)
                .collection(COLLECTION_FAVORITE)
                .document(fav.getEventId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                isFavorite.postValue(true);
                                Log.d("TAG", "Document exists!");

                                db.collection(COLLECTION_USERS)
                                        .document(loggedInUserId)
                                        .collection(COLLECTION_FAVORITE)
                                        .document(fav.getEventId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "Event removed from favorite successfully");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error removing event from favorite document", e);
                                            }
                                        });
                            } else {
                                Log.d("TAG", "Document does not exist!");
                                isFavorite.postValue(false);
                                try {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put(FIELD_EVENT_ID, fav.getEventId());

                                    db.collection(COLLECTION_USERS)
                                            .document(loggedInUserId)
                                            .collection(COLLECTION_FAVORITE)
                                            .document(fav.getEventId()).set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.e("TAG", "onSuccess: Event Added to favorite successfully created");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("TAG", "onFailure: Error while adding to favorite" + e.getLocalizedMessage() );
                                                }
                                            });
                                } catch (Exception e) {
                                    Log.e("TAG", "addToFavorite Exception: " + e.getLocalizedMessage());
                                }
                            }
                        } else {
                            Log.d("TAG", "Failed with: ", task.getException());
                        }
                    }
                });
    }
}

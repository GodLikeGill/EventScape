package com.group5.eventscape.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.eventscape.models.Favorite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteRepository {
    private final FirebaseFirestore db;
    private final String COLLECTION_USERS = "Users";
    private final String COLLECTION_FAVORITE = "favorite";
    private final String FIELD_ID = "id";
    private final String FIELD_EVENT_ID = "eventId";
    public String loggedInUserId = "";

    public MutableLiveData<List<Favorite>> allOrders = new MutableLiveData<>();

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
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.e("TAG", "onSuccess: Event Added to favorite successfully created with ID " + documentReference.getId() );
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure: Error while adding to favorite" + e.getLocalizedMessage() );
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "addToFavorite Exception: " + e.getLocalizedMessage());
        }
    }
}

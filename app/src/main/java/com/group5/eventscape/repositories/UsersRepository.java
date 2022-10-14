package com.group5.eventscape.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.group5.eventscape.models.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersRepository {

    private final FirebaseFirestore db;
    private final String COLLECTION_USERS = "Users";
    private final String FIELD_FULL_NAME = "full_Name";
    private final String FIELD_EMAIL = "email";
    private final String FIELD_ID = "id";

    public MutableLiveData<List<Users>> allUsers = new MutableLiveData<>();

    public UsersRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllUsers() {
        try {
            db.collection(COLLECTION_USERS).get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<Users> usersList = new ArrayList<>();
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("TAG", "getAllUsers: No data retrieved.");
                } else {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        Users users = documentChange.getDocument().toObject(Users.class);
                        usersList.add(users);
                        Log.d("TAG", "getAllUsers: " + users.getFullName());
                    }
                }
                allUsers.postValue(usersList);
            });
        } catch (Exception e) {
            Log.e("TAG", "getAllUsers: " + e.getLocalizedMessage());
        }
    }

    public void addUsers(Users users) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(FIELD_FULL_NAME, users.getFullName());
            db.collection(COLLECTION_USERS).document(users.getId()).set(data).addOnSuccessListener(documentReference -> {
                Log.d("TAG", "addUsers: User created successfully");
            }).addOnFailureListener(e -> {
                Log.e("TAG", "onFailure: Error while creating users " + e.getLocalizedMessage());
            });
        } catch (Exception e) {
            Log.e("TAG", "addUsers: " + e.getLocalizedMessage());
        }
    }
}

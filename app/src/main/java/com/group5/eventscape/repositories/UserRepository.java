package com.group5.eventscape.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.eventscape.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {

    private final FirebaseFirestore db;
    private final String COLLECTION_USERS = "Users";
    private final String FIELD_FULL_NAME = "fullName";
    private final String FIELD_EMAIL = "email";
    private final String FIELD_ID = "id";
    private final String FIELD_BALANCE = "balance";
    private final String FIELD_IMAGE = "image";

    public MutableLiveData<User> currentUser = new MutableLiveData<>();
    public MutableLiveData<List<User>> allUsers = new MutableLiveData<>();

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllUsers() {
        try {
            db.collection(COLLECTION_USERS).get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<User> usersList = new ArrayList<>();
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.e("TAG", "getAllUsers: No data retrieved.");
                } else {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        User users = documentChange.getDocument().toObject(User.class);
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

    public void getCurrentUser(String id) {
        try {
            db.collection(COLLECTION_USERS).document(id).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    currentUser.postValue(documentSnapshot.toObject(User.class));
                } else {
                    Log.e("TAG", "getUser: No data retrieved.");
                }
            }).addOnFailureListener(e -> {
                Log.e("TAG", "getUser: " + e.getLocalizedMessage());
            });
        } catch (Exception e) {
            Log.e("TAG", "getUser: " + e.getLocalizedMessage());
        }
    }

    public void addUser(User user) {
        try {
            Map<String, Object> newUser = new HashMap<>();
            newUser.put(FIELD_FULL_NAME, user.getFullName());
            newUser.put(FIELD_EMAIL, user.getEmail());
            newUser.put(FIELD_ID, user.getId());
            newUser.put(FIELD_IMAGE, user.getImage());
            newUser.put(FIELD_BALANCE, 0.00);
            db.collection(COLLECTION_USERS).document(user.getId()).set(newUser).addOnSuccessListener(documentReference -> {
                Log.d("TAG", "addUser: User created successfully");
            }).addOnFailureListener(e -> {
                Log.e("TAG", "onFailure: Error while creating user." + e.getLocalizedMessage());
            });
        } catch (Exception e) {
            Log.e("TAG", "addUser: " + e.getLocalizedMessage());
        }
    }

    public void updateUser(User user) {
        try {
            Map<String, Object> updatedUser = new HashMap<>();
            updatedUser.put(FIELD_FULL_NAME, user.getFullName());
            updatedUser.put(FIELD_EMAIL, user.getEmail());
            updatedUser.put(FIELD_IMAGE, user.getImage());
            updatedUser.put(FIELD_BALANCE, user.getBalance());
            db.collection(COLLECTION_USERS).document(user.getId()).update(updatedUser).addOnSuccessListener(unused -> {
                Log.d("TAG", "updateUser: User updated successfully");
            }).addOnFailureListener(e -> {
                Log.e("TAG", "updateUser: " + e.getLocalizedMessage());
            });
        } catch (Exception e) {
            Log.e("TAG", "updateUser: " + e.getLocalizedMessage());
        }
    }
}

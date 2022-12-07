package com.group5.eventscape.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.group5.eventscape.models.User;
import com.group5.eventscape.repositories.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository = new UserRepository();
    private static UserViewModel instance;

    public MutableLiveData<List<User>> allUsers;
    public MutableLiveData<User> currentUser;
    public MutableLiveData<User> userByEmail;

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public static UserViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new UserViewModel(application);
        }
        return instance;
    }

    public UserRepository getUsersRepository() {
        return this.repository;
    }

    public void getAllUsers() {
        this.repository.getAllUsers();
        this.allUsers = this.repository.allUsers;
    }

    public void getCurrentUser(String id) {
        this.repository.getCurrentUser(id);
        this.currentUser = this.repository.currentUser;
    }

    public void getUserByEmail(String userEmail) {
        this.repository.getUserByEmail(userEmail);
        this.userByEmail = this.repository.userByEmail;
    }

    public void addUser(User user) {
        this.repository.addUser(user);
    }
    public void updateUser(User user) { this.repository.updateUser(user); }
    public void updateUserBalance(User user) { this.repository.updateUserBalance(user); }
}

package com.group5.eventscape.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.auth.User;
import com.group5.eventscape.models.Users;
import com.group5.eventscape.repositories.UsersRepository;

import java.util.List;

public class UsersViewModel extends AndroidViewModel {

    private final UsersRepository repository = new UsersRepository();
    private static UsersViewModel instance;
    public MutableLiveData<List<Users>> allUsers;

    public UsersViewModel(@NonNull Application application) {
        super(application);
    }

    public static UsersViewModel getInstance(Application application){
        if (instance == null){
            instance = new UsersViewModel(application);
        }
        return instance;
    }

    public UsersRepository getUsersRepository(){
        return this.repository;
    }

    public void getAllUsers(){
        this.repository.getAllUsers();
        this.allUsers = this.repository.allUsers;
    }

    public void addUsers(Users users){ this.repository.addUsers(users); }
}

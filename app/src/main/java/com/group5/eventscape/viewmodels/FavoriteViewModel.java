package com.group5.eventscape.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.group5.eventscape.models.Favorite;
import com.group5.eventscape.repositories.FavoriteRepository;

public class FavoriteViewModel extends AndroidViewModel {
    private final FavoriteRepository repository = new FavoriteRepository();
    private static FavoriteViewModel instance;

    public MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    public MutableLiveData<Favorite> favoriteEvent;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
    }

    public static FavoriteViewModel getInstance(Application application){
        if (instance == null){
            instance = new FavoriteViewModel(application);
        }
        return instance;
    }

    public void addToFavorite(Favorite favorite){ this.repository.addToFavorite(favorite); }

    public void getFavoriteForCurrentEvent(String id) {
        this.repository.getFavoriteForCurrentEvent(id);
        this.favoriteEvent = this.repository.favoriteEvent;
    }

    public void checkForFavorite(Favorite favorite){
        this.repository.checkForFavorite(favorite);
        this.isFavorite = this.repository.isFavorite;

    }

}

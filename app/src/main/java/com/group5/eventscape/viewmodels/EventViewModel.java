package com.group5.eventscape.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.group5.eventscape.models.Event;
import com.group5.eventscape.models.Favorite;
import com.group5.eventscape.repositories.EventRepository;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private final EventRepository repository = new EventRepository();
    private static EventViewModel instance;
    public MutableLiveData<List<Event>> allListings;
    public MutableLiveData<Event> eventById;

    public EventViewModel(@NonNull Application application) {
        super(application);
    }

    public static EventViewModel getInstance(Application application){
        if (instance == null){
            instance = new EventViewModel(application);
        }
        return instance;
    }

    public EventRepository getListingRepository(){
        return this.repository;
    }

    public void getAllEvents(){
        this.repository.getAllEvents();
        this.allListings = this.repository.allEvents;
    }

    public void getEventById(String id) {
        this.repository.getEventById(id);
        this.eventById = this.repository.eventById;
    }

    public void addEvent(Event events){ this.repository.addEvent(events); }
    public void deleteEvent(String id){ this.repository.deleteEvent(id);}
}




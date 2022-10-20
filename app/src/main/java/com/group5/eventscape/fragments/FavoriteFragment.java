package com.group5.eventscape.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.eventscape.R;
import com.group5.eventscape.activities.EventDetailActivity;
import com.group5.eventscape.adapters.EventsAdapter;
import com.group5.eventscape.adapters.FavoriteAdapter;
import com.group5.eventscape.databinding.FragmentFavoriteBinding;
import com.group5.eventscape.databinding.FragmentHomeBinding;
import com.group5.eventscape.interfaces.OnRowClicked;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.models.Favorite;
import com.group5.eventscape.viewmodels.EventViewModel;
import com.group5.eventscape.viewmodels.FavoriteViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements OnRowClicked {

    FragmentFavoriteBinding binding;
    private String TAG = this.getClass().getCanonicalName();

    private ArrayList<Event> eventsList = new ArrayList<Event>();
    private FavoriteAdapter adapter;
    private FavoriteViewModel favoriteViewModel;
    private EventViewModel eventViewModel;



    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // create an instance of the adapter
        this.adapter = new FavoriteAdapter(view.getContext(), this.eventsList, this::onRowClicked);

        // configure the recyclerview to use the adapter
        binding.rvFavoriteEvents.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.rvFavoriteEvents.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        binding.rvFavoriteEvents.setAdapter(this.adapter);

        favoriteViewModel = FavoriteViewModel.getInstance(getActivity().getApplication());
        eventViewModel = EventViewModel.getInstance(getActivity().getApplication());



    }

    @Override
    public void onResume() {
        super.onResume();

        getFavEventsId();
    }

    @Override
    public void onRowClicked(Event events) {
        Log.e(TAG, "EventDetail: Clicked" + events.getId() );
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);

        intent.putExtra("curEvent", events);
        startActivity(intent);
    }

    private void getFavEventsId(){
        this.favoriteViewModel.getAllFav();
        this.favoriteViewModel.allFav.observe(getViewLifecycleOwner(), eventsId ->{
            eventsList.clear();
            for(Favorite eventId : eventsId){
                getEventsById(eventId);
            }
        });

    }



    private void getEventsById(Favorite eventId){
        this.eventViewModel.getEventById(eventId.getEventId());
        this.eventViewModel.eventById.observe(getViewLifecycleOwner(), event ->{

            eventsList.add(event);

        });
    }


}
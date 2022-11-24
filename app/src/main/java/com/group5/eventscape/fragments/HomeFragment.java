package com.group5.eventscape.fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
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

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.group5.eventscape.interfaces.OnRowClicked;
import com.group5.eventscape.activities.EventDetailActivity;
import com.group5.eventscape.adapters.EventsAdapter;
import com.group5.eventscape.databinding.FragmentHomeBinding;
import com.group5.eventscape.helpers.LocationHelper;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.models.Order;
import com.group5.eventscape.viewmodels.EventViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements OnRowClicked {
    private FragmentHomeBinding binding;
    private String TAG = this.getClass().getCanonicalName();

    //Location start
    private LocationHelper locationHelper;
    private Location lastLocation;
    private LocationCallback locationCallback;

    // define the data source for the adapter
    private ArrayList<Event> eventsList = new ArrayList<Event>();
    private EventsAdapter adapter;
    private EventViewModel eventViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();



        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermissions(view.getContext());


        if(this.locationHelper.getLastLocation(view.getContext()) != null){
            this.locationHelper.getLastLocation(view.getContext()).observe(getViewLifecycleOwner(), new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    if (location != null){
                        lastLocation = location;

                        Address obtainedAddress = locationHelper.performForwardGeocoding(view.getContext(), lastLocation);

                        if (obtainedAddress != null){
                            binding.tvCurrentLocation.setText(obtainedAddress.getAddressLine(0));
                        }else{
                            binding.tvCurrentLocation.setText(lastLocation.toString());
                        }
                    }else{
                        binding.tvCurrentLocation.setText("Last location not obtained");
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // create an instance of the adapter
        this.adapter = new EventsAdapter(view.getContext(), this.eventsList, this::onRowClicked);

        // configure the recyclerview to use the adapter
        binding.rvEvents.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.rvEvents.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        binding.rvEvents.setAdapter(this.adapter);

        eventViewModel = EventViewModel.getInstance(getActivity().getApplication());

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        this.getAllEvents(view);
    }

    private void filter(String searchString){
        ArrayList<Event> filteredEventList = new ArrayList<>();

        for (Event item : eventsList){
            if(item.getTitle().toLowerCase().contains(searchString.toLowerCase())){
                filteredEventList.add(item);
            }
        }
        adapter.filteredList(filteredEventList);
    }

    @Override
    public void onRowClicked(Event events) {
        Log.e(TAG, "EventDetail: Clicked" + events.getId() );
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);

        intent.putExtra("curEvent", events);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
        this.locationHelper.stopLocationUpdates(getView().getContext(), this.locationCallback);
        this.getAllEvents(getView());
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
        this.locationHelper.getLocationUpdates(getView().getContext(), this.locationCallback);
        this.getAllEvents(getView());
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop");
        this.locationHelper.stopLocationUpdates(getView().getContext(), this.locationCallback);
        this.getAllEvents(getView());
        //this.adapter.notifyDataSetChanged();
    }

    private void getAllEvents(View view){
        this.eventViewModel.getAllEvents();
        this.eventViewModel.allListings.observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                if (events.isEmpty()){
                    Log.e(TAG, "onChanged: No documents received");
                }else{
//                    for(Event event : events){
//                        Log.e(TAG, "onChanged: event : " + events.toString() );
//                    }

                    eventsList.clear();

                    //filterEventByDate(events);

                    List<Event> sortedList = filterEventByDate(events);
                    Comparator<Event> eventDateSorter
                            = (o1, o2) -> o1.getDate().compareTo(o2.getDate());

                    Collections.sort(sortedList, eventDateSorter);
                    //Collections.reverse(sortedList);
                    //myOrders.addAll(sortedList);


                    eventsList.addAll(sortedList);
                    initiateLocationListener(view);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initiateLocationListener(View view){
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location loc : locationResult.getLocations()){
                    lastLocation = loc;

                    Log.d(TAG, "onLocationResult: updated location : " + lastLocation.toString() );

                    Address obtainedAddress = locationHelper.performForwardGeocoding(view.getContext(), lastLocation);

                    if (obtainedAddress != null){
                        //binding.tvCurrentLocation.setText(obtainedAddress.getAddressLine(0));
                        //appendDistanceToUser(lastLocation);
                    }else{
                        binding.tvCurrentLocation.setText(lastLocation.toString());
                        //appendDistanceToUser(lastLocation);
                    }
                }
            }
        };

        this.locationHelper.getLocationUpdates(view.getContext(), locationCallback);
    }

    private List<Event> filterEventByDate(List<Event> events){
        ArrayList<Event> filteredEventList = new ArrayList<Event>();
        Date date = new Date();
        long curDateMilli = date.getTime();

        filteredEventList.clear();

        for(Event event : events){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date eDate = null;
            try {
                eDate = sdf.parse(event.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long eDateMillis = eDate.getTime();
            //Log.e(TAG, "filterEventByDate: " + eDateMillis );
            if(eDateMillis > curDateMilli){
                filteredEventList.add(event);
            }
        }

        return filteredEventList;
    }
}
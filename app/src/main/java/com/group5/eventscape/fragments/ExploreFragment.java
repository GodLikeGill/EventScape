package com.group5.eventscape.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.group5.eventscape.R;
import com.group5.eventscape.adapters.EventsAdapter;
import com.group5.eventscape.databinding.FragmentExploreBinding;
import com.group5.eventscape.databinding.FragmentHomeBinding;
import com.group5.eventscape.helpers.LocationHelper;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.viewmodels.EventViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment implements OnMapReadyCallback{
    private FragmentExploreBinding binding;
    private String TAG = this.getClass().getCanonicalName();
    private Boolean firstLoad = true;

    private GoogleMap mMap;
    private LatLng currentLocation;
    private LocationHelper locationHelper;
    private LocationCallback locationCallback;
    private Location lastLocation;

    // define the data source for the adapter
    private ArrayList<Event> eventsList = new ArrayList<Event>();
    private EventViewModel eventViewModel;


    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermissions(view.getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventViewModel = EventViewModel.getInstance(getActivity().getApplication());








        if (locationHelper.locationPermissionGranted){
            this.setLocation(view);
        }




        // initiate and perform click event on button's
        ImageButton imgBtnCurrentLocation = (ImageButton) binding.imgBtnCurrentLocation;
        imgBtnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation(view);
                Toast.makeText(getContext(),"Current location set", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton imgBtnGo = (ImageButton) binding.imgBtnGo;
        imgBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doReverseGeocoding(view);
                Toast.makeText(getContext(),"New location set", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setLocation(View view) {
        this.locationHelper.getLastLocation(view.getContext()).observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if (location != null){
                    lastLocation = location;
                    currentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    Address obtainedAddress = locationHelper.performForwardGeocoding(view.getContext(), lastLocation);
                    if (obtainedAddress != null){
                        binding.etLocation.setText(obtainedAddress.getAddressLine(0));
                    }
                    Log.e(TAG, "onChanged: LastLocation NK" + location);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
                }else{
                    Log.e(TAG, "onChanged: Last location not obtained");
                }
            }
        });
    }



    private void doReverseGeocoding(View view){
        Log.d(TAG, "onClick: Perform reverse geocoding to get coordinates from address.");

        if (this.locationHelper.locationPermissionGranted){
            String givenAddress = this.binding.etLocation.getText().toString();
            LatLng obtainedCoords = this.locationHelper.performReverseGeocoding(getActivity(), givenAddress);

            if (obtainedCoords != null){
                this.setSearchLocation(view, obtainedCoords.latitude, obtainedCoords.longitude);
                this.lastLocation = new Location(obtainedCoords.toString());
            }else{
                Toast.makeText(getContext(),"No coordinates obtained", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getContext(),"Couldn't get coordinates from address", Toast.LENGTH_LONG).show();
        }
    }

    private void setSearchLocation(View view, double latitude, double longitude) {
        currentLocation = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
    }



    //private void getAllEvents(View view, GoogleMap mMap) {
    private void getAllEvents(View view) {
        this.eventViewModel.getAllEvents();
        this.eventViewModel.allListings.observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                if (events.isEmpty()){
                    Log.e(TAG, "onChanged: No documents received");
                }else{
                    for(Event event : events){
                        if(event.getLatitude() != null){
                            mMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(event.getLatitude()) , Float.parseFloat(event.getLongitude()))).title("EventScape Event: " + event.getTitle())).setIcon(BitmapFromVector(getContext(), R.drawable.ic_map_marker_48));
                        }
                        //Log.e(TAG, "onChanged: event : " + event.getTitle() );
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(43.6512977, -79.3702414);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));
//
//        LatLng sydney2 = new LatLng(43.6762309, -79.4102862);
//        mMap.addMarker(new MarkerOptions().position(sydney2).title("Current Location 2"));

        //this.getAllEvents(getView(), mMap);
        this.getAllEvents(getView());

        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(false);

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
        //this.getAllEvents(getView());
        //this.locationHelper.stopLocationUpdates(getView().getContext(), this.locationCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
        this.getAllEvents(getView());
        //this.locationHelper.getLocationUpdates(getView().getContext(), this.locationCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop");
        this.getAllEvents(getView());
        //this.locationHelper.stopLocationUpdates(getView().getContext(), this.locationCallback);
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
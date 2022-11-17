package com.group5.eventscape.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

public class LocationHelper {

    private final String TAG = this.getClass().getCanonicalName();
    public boolean locationPermissionGranted = false;
    public final int REQUEST_CODE_LOCATION = 101;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    Location myLocationOnce;
    MutableLiveData<Location> myLocation = new MutableLiveData<>();
    private LocationRequest locationRequest;

    private static final LocationHelper instance = new LocationHelper();
    public static LocationHelper getInstance(){
        return instance;
    }

    private LocationHelper(){
        this.locationRequest = new LocationRequest();
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationRequest.setInterval(10000);
    }

    public void checkPermissions(Context context){
        this.locationPermissionGranted =
                (PackageManager.PERMISSION_GRANTED == (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)));

        Log.d(TAG, "checkPermissions: LocationPermissionGranted " + this.locationPermissionGranted );

        if (!this.locationPermissionGranted){
            //request permission
            requestLocationPermission(context);
        }
    }

    public void requestLocationPermission(Context context){
        ActivityCompat.requestPermissions( (Activity) context,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_LOCATION);
    }

    public FusedLocationProviderClient getFusedLocationProviderClient(Context context){
        if (this.fusedLocationProviderClient == null){
            this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }

        return this.fusedLocationProviderClient;
    }

    public MutableLiveData<Location> getLastLocation(Context context){
        if (this.locationPermissionGranted){
            Log.d(TAG, "getLastLocation: Permission is granted...obtaining last location now");

            try{

                this.getFusedLocationProviderClient(context)
                        .getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null){
//                                    myLocation = new Location(location);
//                                    Log.d(TAG, "onSuccess: Last location obtained Lat : " + myLocation.getLatitude() + " Lng : " + myLocation.getLongitude() );

                                    myLocation.setValue(location);
                                    Log.d(TAG, "onSuccess: Last location obtained Lat : " + myLocation.getValue().getLatitude() + " Lng : " + myLocation.getValue().getLongitude() );
                                }else{
                                    Log.d(TAG, "onSuccess: Unable to access last location");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Failed to get the last location" + e.getLocalizedMessage() );
                            }
                        });

            }catch (Exception ex){
                Log.d(TAG, "getLastLocation: Exception occurred while fetching last location " + ex.getLocalizedMessage() );
                return  null;
            }

            return this.myLocation;
        }else{
            Log.d(TAG, "getLastLocation: Location permission not granted");
            requestLocationPermission(context);
            return null;
        }
    }


    public Location getLastLocationOnce(Context context){
        if (this.locationPermissionGranted){
            Log.e(TAG, "getLastLocationOnce: Permission is granted...obtaining last location now");

            try{
                this.getFusedLocationProviderClient(context)
                        .getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null){
                                    myLocationOnce = new Location(location);
                                    Log.e(TAG, "getLastLocationOnce: Kumbhare not null" );
                                    Log.e(TAG, "onSuccess: Last location obtained Lat : " + myLocationOnce.getLatitude() + " Lng : " + myLocationOnce.getLongitude() );
                                }else{
                                    Log.e(TAG, "getLastLocationOnce: Kumbhare null" );
                                    Log.d(TAG, "onSuccess: Unable to access last location");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Failed to get the last location" + e.getLocalizedMessage() );
                            }
                        });
            }catch (Exception ex){
                Log.d(TAG, "getLastLocation: Exception occurred while fetching last location " + ex.getLocalizedMessage() );
                return  null;
            }
            return this.myLocationOnce;
        }else{
            Log.d(TAG, "getLastLocation: Location permission not granted");
            requestLocationPermission(context);
            return null;
        }
    }




    public Address performForwardGeocoding(Context context, Location loc){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;

        try{

            addressList = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            if (addressList.size() > 0){
                Address addressObj = addressList.get(0);

                return  addressObj;
            }

        }catch (Exception ex){
            Log.d(TAG, "performForwardGeocoding: Couldn't get the address for the given location coordinates" + ex.getLocalizedMessage() );
        }

        return null;
    }

    //    public Location
    public LatLng performReverseGeocoding(Context context, String address){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try{

            List<Address> locationList = geocoder.getFromLocationName(address, 1);

            if (locationList.size() > 0){
                LatLng obtainedCoords = new LatLng(locationList.get(0).getLatitude(), locationList.get(0).getLongitude());

                Log.d(TAG, "performReverseGeocoding: Obtained Coords : " + obtainedCoords.toString() );
                return  obtainedCoords;
            }

        }catch (Exception ex){
            Log.d(TAG, "performReverseGeocoding: Couldn't get the LatLng for the given address" + ex.getLocalizedMessage() );
        }

        return  null;
    }

    @SuppressLint("MissingPermission")
    public void getLocationUpdates(Context context, LocationCallback locationCallback){
        if (locationPermissionGranted){
            try{
                this.getFusedLocationProviderClient(context).requestLocationUpdates(this.locationRequest, locationCallback, Looper.getMainLooper());
            }catch (Exception ex){
                Log.d(TAG, "getLocationUpdates: Exception occurred while receiving location updates" + ex.getLocalizedMessage() );
            }
        }else{
            Log.d(TAG, "getLocationUpdates: location permission not granted");
        }
    }

    public void stopLocationUpdates(Context context, LocationCallback locationCallback){
        try{
            this.getFusedLocationProviderClient(context).removeLocationUpdates(locationCallback);
        }catch (Exception ex){
            Log.d(TAG, "stopLocationUpdates: Exception occurred while removing location updates" + ex.getLocalizedMessage() );
        }
    }
}


/*{
    private final String TAG = this.getClass().getCanonicalName();
    public boolean locationPermissionGranted = false;
    public final int REQUEST_CODE_LOCATION = 101;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    //    Location myLocation;
    MutableLiveData<Location> myLocation = new MutableLiveData<>();
    private LocationRequest locationRequest;

    private static final LocationHelper instance = new LocationHelper();
    public static LocationHelper getInstance(){
        return instance;
    }

    private LocationHelper(){
        this.locationRequest = new LocationRequest();
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationRequest.setInterval(10000);
    }

    public void checkPermissions(Context context){
        this.locationPermissionGranted =
                (PackageManager.PERMISSION_GRANTED == (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)));

        Log.d(TAG, "checkPermissions: LocationPermissionGranted " + this.locationPermissionGranted );

        if (!this.locationPermissionGranted){
            //request permission
            requestLocationPermission(context);
        }
    }

    public void requestLocationPermission(Context context){
        ActivityCompat.requestPermissions( (Activity) context,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_LOCATION);
    }

    public FusedLocationProviderClient getFusedLocationProviderClient(Context context){
        if (this.fusedLocationProviderClient == null){
            this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }

        return this.fusedLocationProviderClient;
    }

    //    public Location getLastLocation(Context context){
    public MutableLiveData<Location> getLastLocation(Context context){
        if (this.locationPermissionGranted){
            Log.d(TAG, "getLastLocation: Permission is granted...obtaining last location now");

            try{

                this.getFusedLocationProviderClient(context)
                        .getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null){
//                                    myLocation = new Location(location);
//                                    Log.d(TAG, "onSuccess: Last location obtained Lat : " + myLocation.getLatitude() + " Lng : " + myLocation.getLongitude() );

                                    myLocation.setValue(location);
                                    Log.d(TAG, "onSuccess: Last location obtained Lat : " + myLocation.getValue().getLatitude() + " Lng : " + myLocation.getValue().getLongitude() );
                                }else{
                                    Log.d(TAG, "onSuccess: Unable to access last location");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Failed to get the last location" + e.getLocalizedMessage() );
                            }
                        });

            }catch (Exception ex){
                Log.d(TAG, "getLastLocation: Exception occurred while fetching last location " + ex.getLocalizedMessage() );
                return  null;
            }

            return this.myLocation;
        }else{
            Log.d(TAG, "getLastLocation: Location permission not granted");
            requestLocationPermission(context);
            return null;
        }
    }


    public Address performForwardGeocoding(Context context, Location loc){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;

        try{

            addressList = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            if (addressList.size() > 0){
                Address addressObj = addressList.get(0);

                return  addressObj;
            }

        }catch (Exception ex){
            Log.d(TAG, "performForwardGeocoding: Couldn't get the address for the given location coordinates" + ex.getLocalizedMessage() );
        }

        return null;
    }

    //    public Location
    public LatLng performReverseGeocoding(Context context, String address){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try{

            List<Address> locationList = geocoder.getFromLocationName(address, 1);

            if (locationList.size() > 0){
                LatLng obtainedCoords = new LatLng(locationList.get(0).getLatitude(), locationList.get(0).getLongitude());

                Log.d(TAG, "performReverseGeocoding: Obtained Coords : " + obtainedCoords.toString() );
                return  obtainedCoords;
            }

        }catch (Exception ex){
            Log.d(TAG, "performReverseGeocoding: Couldn't get the LatLng for the given address" + ex.getLocalizedMessage() );
        }

        return  null;
    }

    @SuppressLint("MissingPermission")
    public void getLocationUpdates(Context context, LocationCallback locationCallback){
        if (locationPermissionGranted){
            try{
                this.getFusedLocationProviderClient(context).requestLocationUpdates(this.locationRequest, locationCallback, Looper.getMainLooper());
            }catch (Exception ex){
                Log.d(TAG, "getLocationUpdates: Exception occurred while receiving location updates" + ex.getLocalizedMessage() );
            }
        }else{
            Log.d(TAG, "getLocationUpdates: location permission not granted");
        }
    }

    public void stopLocationUpdates(Context context, LocationCallback locationCallback){
        try{
            this.getFusedLocationProviderClient(context).removeLocationUpdates(locationCallback);
        }catch (Exception ex){
            Log.d(TAG, "stopLocationUpdates: Exception occurred while removing location updates" + ex.getLocalizedMessage() );
        }
    }
}*/

package ro.taxi_ineu.taxiineu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import ro.taxi_ineu.taxiineu.databinding.ActivityMapBinding;

//public class MapsActivityCurrentPlace extends AppCompatActivity implements OnMapReadyCallback {
//public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    public static final int Deafult_update_interval = 10;//30
    public static final int Fast_update_interval = 5;
    //public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    //public static final int PRIORITY_LOW_POWER = 104;
    //public static final int PRIORITY_NO_POWER = 105;
    //private static final int PERMISSION_FINE_LOCATION = 99;
    //Location request is a config file for all settings related to FusedLocationProviderClient
    private LocationRequest locationRequest;
    private Marker marker1;
    Double latitude_glob,longitude_glob;
    String buff="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set all properties of LocationRequest
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * Deafult_update_interval);//5000
        locationRequest.setFastestInterval(1000 * Fast_update_interval);//2000
        locationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
        //locationRequest.setPriority(PRIORITY_BALANCED_POWER_ACCURACY);



        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }//oncreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_map,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String buff="";
        String lat,lon,adresa;
        double a,b;
        a=marker1.getPosition().latitude;
        b=marker1.getPosition().longitude;
        lat=String.valueOf(a);
        lon=String.valueOf(b);
        adresa=getCurrentAddress(a,b);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_home:
                // write all the data entered by the user in SharedPreference and apply
                myEdit.putString("latitude1", lat);
                myEdit.putString("longitude1", lon);
                myEdit.putString("adresa1", adresa);
                //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
                myEdit.apply();
                buff=getResources().getString(R.string.saveButton)+" " +getResources().getString(R.string.radioButton1);
                Toast.makeText(MapActivity.this, buff, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_work:
                // write all the data entered by the user in SharedPreference and apply
                myEdit.putString("latitude2", lat);
                myEdit.putString("longitude2", lon);
                myEdit.putString("adresa2", adresa);
                //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
                myEdit.apply();

                buff=getResources().getString(R.string.saveButton)+" " +getResources().getString(R.string.radioButton2);
                Toast.makeText(MapActivity.this, buff, Toast.LENGTH_SHORT).show();

                return true;
            case R.id.action_fav:
                // write all the data entered by the user in SharedPreference and apply
                myEdit.putString("latitude3", lat);
                myEdit.putString("longitude3", lon);
                myEdit.putString("adresa3", adresa);
                //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
                myEdit.apply();

                buff=getResources().getString(R.string.saveButton)+" " +getResources().getString(R.string.radioButton3);
                Toast.makeText(MapActivity.this, buff, Toast.LENGTH_SHORT).show();

                return true;
            case R.id.action_gps:
                getCurrentLocation();
                buff=getResources().getString(R.string.action_gps);
                Toast.makeText(MapActivity.this, buff, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng taxiineu1 = new LatLng(46.423823039121835, 21.829250939190388);
        //LatLng taxiineu1 = new LatLng(46, 21);
        marker1=mMap.addMarker(new MarkerOptions().position(taxiineu1).title("TaxiIneu").draggable(true));
        marker1.showInfoWindow();
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(taxiineu1));
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(taxiineu1,12 ));

        //taxiineu2=new LatLng(45.423823039121835,22.829250939190388);

        //CameraUpdate center=CameraUpdateFactory.newLatLng(taxiineu2);
        //CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);

        //mMap.moveCamera(center);
        //mMap.animateCamera(zoom);

        buff=String.valueOf(taxiineu1.latitude)+" "+String.valueOf(taxiineu1.longitude);
        System.out.println("first:" + buff);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                double a,b;
                String adr;
                a=marker.getPosition().latitude;
                b=marker.getPosition().longitude;
                adr = getCurrentAddress(a,b);
                marker.setTitle(adr);
                marker.showInfoWindow();
                //buff=String.valueOf(a)+" "+String.valueOf(b);
                //System.out.println("buff:" + buff);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                double a,b;
                String adr;
                a=marker.getPosition().latitude;
                b=marker.getPosition().longitude;
                adr = getCurrentAddress(a,b);
                marker.setTitle(adr);
                marker.showInfoWindow();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }
    private void update_marker(Double latitude, Double longitude){
        String adr;
        adr=getCurrentAddress(latitude, longitude);
        LatLng gps_location = new LatLng(latitude, longitude);
        //mMap.addMarker(new MarkerOptions().position(sydney1).title("Marker in Sydney11").draggable(true));
        marker1.setPosition(gps_location);
        marker1.setTitle(adr);
        marker1.showInfoWindow();
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(gps_location,18 ));
        //marker1.setDraggable(true);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(gps_location));
        //mMap.animateCamera( CameraUpdateFactory.zoomTo( 18.0f ) );
    }
    private String getCurrentAddress(Double latitude, Double longitude) {
        Geocoder geocoder = new Geocoder(MapActivity.this);
        String adresa2;
        try {
            List<Address> adresses= geocoder.getFromLocation(latitude, longitude,1);
            adresa2=adresses.get(0).getAddressLine(0);
            //Tv_status.setText(adresses.get(0).getAddressLine(0));
        }
        catch (Exception e) {
            adresa2=getResources().getString(R.string.no_address);
        }
        //adresa=adresa2;
        return adresa2;
    }
    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MapActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MapActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){
                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        double accuracy = locationResult.getLocations().get(index).getAccuracy();
                                        String altitude = getString(R.string.not_available);
                                        if (locationResult.getLocations().get(index).hasAltitude()){
                                            double altitude2 = locationResult.getLocations().get(index).getAltitude();
                                            altitude=String.valueOf(altitude2);
                                        }

                                        //update_marker(latitude,longitude,altitude,accuracy);
                                        update_marker(latitude,longitude);
                                        latitude_glob = latitude;
                                        longitude_glob = longitude;

                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MapActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MapActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }


}
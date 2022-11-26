package ro.taxi_ineu.taxiineu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.app.Activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

//php
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;


import java.security.MessageDigest;
import java.util.List;


import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

//ssl
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import java.security.cert.CertificateException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.cert.CertificateFactory;
import java.io.BufferedInputStream;
import java.security.SecureRandom;
import java.util.UUID;

//notification
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import java.util.Timer;
import java.util.TimerTask;
//rate bar
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {
    public static final int Deafult_update_interval = 10;//30
    public static final int Fast_update_interval = 5;
    //public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    //public static final int PRIORITY_LOW_POWER = 104;
    //public static final int PRIORITY_NO_POWER = 105;
    //private static final int PERMISSION_FINE_LOCATION = 99;
    private TextView Tv_lat, Tv_lon, Tv_altitude, Tv_accuracy, Tv_address, Maplink, Tv_status,Tv_titlu,Tv_labelaltitude,Tv_labelaccuracy, Tv_ratingBar, Tv_labelRatingBar;

    private SwitchCompat Switch2;
    private EditText EditTextObs;
    private RatingBar RatingBar;

    double latitude_glob = 0;
    double longitude_glob = 0;

    private String auth_key;
    private String uniqueID;
    private Integer flag=-1;


    //Google's API for location services
    //FusedLocationProviderClient fusedLocationProviderClient;

    //Location request is a config file for all settings related to FusedLocationProviderClient
    private LocationRequest locationRequest;

    //php
    //private List<Product> products;
    private ProgressBar progressBar;
    private static  final String BASE_URL = "https://android.taxi-ineu.ro/getClient.php";
    private static  final String urlsend = "https://android.taxi-ineu.ro/sendClient.php";
    private String last_order_date="";
    //private static  final String auth_key ="PVcJ5UXkAtySVxNMq8";
    //private ActionBar mActionBar;
    //private RecyclerView recyclerView;
    //private RecyclerView.LayoutManager manager;
    //private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button LocationButton,UpdateButton,SendButton,DeleteButton;

        Tv_lat = findViewById(R.id.tv_lat);
        Tv_lon = findViewById(R.id.tv_lon);
        Tv_altitude = findViewById(R.id.tv_altitude);
        Tv_accuracy = findViewById(R.id.tv_accuracy);
        Tv_labelaccuracy = findViewById(R.id.tv_labelaccuracy);
        Tv_labelaltitude = findViewById(R.id.tv_labelaltitude);
        Tv_address = findViewById(R.id.tv_address);
        Maplink = findViewById(R.id.maplink);
        Tv_status=findViewById(R.id.tv_status);
        Tv_titlu=findViewById(R.id.tv_titlu);
        Tv_ratingBar=findViewById(R.id.tv_ratingBar);
        Tv_labelRatingBar=findViewById(R.id.tv_labelRatingBar);
        LocationButton = findViewById(R.id.locationButton);
        SendButton = findViewById(R.id.sendButton);
        UpdateButton = findViewById(R.id.updateButton);
        DeleteButton = findViewById(R.id.deleteButton);
        Switch2 = findViewById(R.id.switch2);
        EditTextObs = findViewById(R.id.editTextTextPersonName);
        RatingBar = findViewById(R.id.ratingBar);

        // Set all properties of LocationRequest
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * Deafult_update_interval);//5000
        locationRequest.setFastestInterval(1000 * Fast_update_interval);//2000
        locationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
        //locationRequest.setPriority(PRIORITY_BALANCED_POWER_ACCURACY);

        //php
        progressBar = findViewById(R.id.progressBar);
        //products = new ArrayList<>();
        progressBar.setVisibility(View.GONE);

        //merge accepta toate certificatele si serverele
        NukeSSLCerts.nuke();

        //createNotificationChannel();

        uniqueID=get_id();
        //auth_key=Base64.encodeToString(uniqueID.getBytes(StandardCharsets.UTF_8),Base64.DEFAULT);
        auth_key=md5(uniqueID+"TaxiIneu");
        System.out.println("uniqueID="+uniqueID+" auth_key="+auth_key);

        Timer timer = new Timer();
        TimerTask task  = new TimerTask() {

            @Override
            public void run() {
                uniqueID=get_id();
                auth_key=md5(uniqueID+"taxiineu");
                getOrders2 (auth_key,uniqueID);
                getRate (auth_key,uniqueID);
            }
        };
        timer.scheduleAtFixedRate(task, 0,5000);

        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,R.string.locationButton, Toast.LENGTH_SHORT).show();
                getCurrentLocation();
                Tv_titlu.setText(R.string.locationButton);
                //sendProducts();
            }
        });

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume, tel, lat, lon, adresa, link, obs;

                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                nume = sh.getString("nume", "");
                tel = sh.getString("tel", "");
                lat=String.valueOf(Tv_lat.getText());
                lon=String.valueOf(Tv_lon.getText());
                adresa=String.valueOf(Tv_address.getText());
                link=String.valueOf(Maplink.getText());
                obs=String.valueOf(EditTextObs.getText());

                //Tv_status.setText(R.string.sendButton);
                uniqueID=get_id();
                auth_key=md5(uniqueID+"TaxiIneu");
                sendOrders(auth_key, uniqueID, nume, tel, lat, lon, adresa, link, obs);
                //Toast.makeText(MainActivity.this,R.string.sendButton, Toast.LENGTH_SHORT).show();
            }
        });
        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,R.string.updateButton, Toast.LENGTH_SHORT).show();

                uniqueID=get_id();
                auth_key=md5(uniqueID+"taxiineu");
                getOrders (auth_key,uniqueID);

                //Tv_status.setText(R.string.updateButton);
                //Tv_status.setText("There is no need for wrapping in a ScrollView. Easily the best and most appropriate answer, especially considering that EditText is a TextView subclass and the question is how to make the textview scrollable, not how to work around the problem");
                //sendProducts();
            }
        });
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,R.string.deleteButton, Toast.LENGTH_SHORT).show();
                uniqueID=get_id();
                auth_key=md5(uniqueID+"TaxiIneu");
                deleteOrders(auth_key, uniqueID);
                /*String name = "TaxiIneu_channel";
                String CHANNEL_ID="TaxiIneu_CN_Id";
                //CHANNEL_ID="MYCHANNEL";
                deleteNotificationChannel(CHANNEL_ID);
                */
                //Tv_status.setText(R.string.deleteButton);
                //Tv_status.setText("There is no need for wrapping in a ScrollView. Easily the best and most appropriate answer, especially considering that EditText is a TextView subclass and the question is how to make the textview scrollable, not how to work around the problem");
                //sendProducts();
            }
        });

        Switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean status;
                status=Switch2.isChecked();
                if(status) {
                    EditTextObs.setEnabled(true);
                    EditTextObs.setHint(R.string.editTextObs);

                }
                else  {
                    EditTextObs.setText("");
                    EditTextObs.setEnabled(false);
                    EditTextObs.setHint("");
                }

            }
        });

        RatingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Toast.makeText(MainActivity.this,R.string.deleteButton, Toast.LENGTH_SHORT).show();
                String buff;
                Float rate;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        rate = RatingBar.getRating();
                        buff = rate.toString();
                        Tv_ratingBar.setText(buff);
                        break;
                    case MotionEvent.ACTION_UP:
                        //start popup
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(getResources().getString(R.string.tv_labelRatingBar));
                        builder.setCancelable(false);
                        final EditText input = new EditText(MainActivity.this);
                        //final TextView input = new TextView(MainActivity.this);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        builder.setView(input);
                        input.setEnabled(false);
                        buff=Tv_ratingBar.getText().toString();
                        input.setText(buff);

                        builder.setNegativeButton(getResources().getString(R.string.pop_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setPositiveButton(getResources().getString(R.string.pop_send), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String m_Text;
                                Float nota;
                                Integer nota1;
                                m_Text = input.getText().toString();
                                nota=Float.parseFloat(m_Text)*10;
                                nota1=(int)(nota + 0.5);
                                m_Text=nota1.toString();
                                uniqueID=get_id();
                                auth_key=md5(uniqueID+"TaxiIneu");
                                rateOrders(auth_key, uniqueID, m_Text);
                                //Tv_ratingBar.setText(m_Text);
                                //Toast.makeText(SettingsActivity.this,"log_out_success"+m_Text, Toast.LENGTH_SHORT).show();

                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                        //stop popup
                        break;
                }
                return RatingBar.onTouchEvent(event);
            }
        });

    }//end void onCreate method

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String get_id(){
        String id;
        //UUID.randomUUID().toString();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        id = sh.getString("uniqueID", "");
        //id =String.valueOf(sh.getBoolean("tel5",false));
        if(id.length()<32) //length=36
            {
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            id=UUID.randomUUID().toString();
            // write all the data entered by the user in SharedPreference and apply
            myEdit.putString("uniqueID", id);
            myEdit.apply();
            }
        return id;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1, s2, s3;
        switch (item.getItemId()){
            case R.id.action_settings:
                //Toast.makeText(this,"Setari selectat", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_home:
                Toast.makeText(this,R.string.action_home, Toast.LENGTH_SHORT).show();
                Tv_titlu.setText(R.string.action_home);
                s1 = sh.getString("latitude1", "");
                s2 = sh.getString("longitude1", "");
                s3 = sh.getString("adresa1", "");
                updateUIValues2(s1,s2,s3);
                return true;
            case R.id.action_work:
                Toast.makeText(this,R.string.action_work, Toast.LENGTH_SHORT).show();
                Tv_titlu.setText(R.string.action_work);
                s1 = sh.getString("latitude2", "");
                s2 = sh.getString("longitude2", "");
                s3 = sh.getString("adresa2", "");
                updateUIValues2(s1,s2,s3);
                return true;
            case R.id.action_fav:
                Toast.makeText(this,R.string.action_fav, Toast.LENGTH_SHORT).show();
                Tv_titlu.setText(R.string.action_fav);
                s1 = sh.getString("latitude3", "");
                s2 = sh.getString("longitude3", "");
                s3 = sh.getString("adresa3", "");
                updateUIValues2(s1,s2,s3);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void stopLocationUpdates() {
        //Tv_updates.setText(R.string.not_tracking);
        Tv_lat.setText(R.string.not_tracking);
        Tv_lon.setText(R.string.not_tracking);
        Tv_address.setText(R.string.not_tracking);
        Tv_accuracy.setText(R.string.not_tracking);
        Tv_altitude.setText(R.string.not_tracking);


    }

    private void updateUIValues (Double latitude, Double longitude, String altitude, Double accuracy) {
        //update all of the text view objects with a new location
        String link;
        Tv_labelaccuracy.setText(R.string.tv_labelaccuracy);
        Tv_labelaltitude.setText(R.string.tv_labelaltitude);
        Tv_lat.setText(String.valueOf(latitude));
        Tv_lon.setText(String.valueOf(longitude));

        link="https://www.google.ro/maps/place/"+latitude+"+"+longitude;
        Maplink.setText(link);

        Tv_accuracy.setText(String.valueOf(accuracy));

        Tv_altitude.setText(altitude);


        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            List<Address> adresses= geocoder.getFromLocation(latitude, longitude,1);
            Tv_address.setText(adresses.get(0).getAddressLine(0));
        }
        catch (Exception e) {
            Tv_address.setText(R.string.no_address);
        }



    }

    private void updateUIValues2 (String latitude, String longitude, String address) {
        //update all of the text view objects with a new location
        String link;
        Tv_labelaccuracy.setText(R.string.tv_labelaccuracy);
        Tv_labelaltitude.setText(R.string.tv_labelaltitude);
        Tv_lat.setText(String.valueOf(latitude));
        Tv_lon.setText(String.valueOf(longitude));

        link="https://www.google.ro/maps/place/"+latitude+"+"+longitude;
        Maplink.setText(link);

        Tv_accuracy.setText(R.string.tv_accuracy);

        Tv_altitude.setText(R.string.tv_altitude);

        Tv_address.setText(address);
    }

    private void updateUIValues3 (String latitude, String longitude, String address, String nume, String tel) {
        //update all of the text view objects with a new location
        String link;
        Tv_labelaccuracy.setText(R.string.textTel);
        Tv_labelaltitude.setText(R.string.textNume);
        Tv_lat.setText(String.valueOf(latitude));
        Tv_lon.setText(String.valueOf(longitude));

        link="https://www.google.ro/maps/place/"+latitude+"+"+longitude;
        Maplink.setText(link);

        Tv_accuracy.setText(tel);

        Tv_altitude.setText(nume);

        Tv_address.setText(address);
    }


    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
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

                                        updateUIValues(latitude,longitude,altitude,accuracy);
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
                    Toast.makeText(MainActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
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


    private void getOrders (String key, String mac){
        progressBar.setVisibility(View.VISIBLE);
        String BASE_URL2;
        BASE_URL2=BASE_URL+"?key="+key+"&mac="+mac+"&opt=tot";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        try {

                            String buff=getResources().getString(R.string.no_order)+"!";
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);

                                //int id = object.getInt("id");
                                String lat = object.getString("lat");
                                String lon = object.getString("lon");
                                String nume = object.getString("nume");
                                String tel = object.getString("tel");
                                String adresa = object.getString("adresa");
                                String data_start = object.getString("data_start");
                                String data_comanda = object.getString("data_comanda");
                                String data_client = object.getString("data_client");
                                String nr_masina = object.getString("nr_masina");
                                Integer status = object.getInt("status");
                                String obs = object.getString("obs");
                                String obs_sofer = object.getString("obs_sofer");
                                String obs_sofer1 = object.getString("obs_sofer1");
                                //String link = object.getString("link");
                                if(obs_sofer.equals("null"))
                                    obs_sofer="";
                                if(obs_sofer1.equals("null"))
                                    obs_sofer1="";

                                Tv_titlu.setText(R.string.update_titlu);
                                updateUIValues3(lat, lon, adresa, nume, tel);
                                buff=data_comanda+"\n"+getResources().getString(R.string.order_received)+". ("+obs+")\n\n";

                                if(status>0) {
                                    buff = buff + data_start + "\n" + getResources().getString(R.string.car_on_way) + ". (" + nr_masina + ")\n"+obs_sofer+"\n\n";
                                }
                                if(status>1) {
                                    buff = buff + data_client + "\n" + getResources().getString(R.string.car_arrived) + "!\n"+obs_sofer1+"\n";
                                }


                            }
                            Tv_status.setText(buff);
                        }catch (Exception e){
                            Tv_status.setText("catch"+e.getMessage());
                            Toast.makeText(MainActivity.this, response,Toast.LENGTH_LONG).show();

                        }

                        //mAdapter = new RecyclerAdapter(MainActivity.this,products);
                        //recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });


/*
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(getSocketFactory(MainActivity.this));
            Tv_status.setText("ssl");
        }catch (Exception e){
            Tv_status.setText(e.getMessage());
        }
//*/
        Volley.newRequestQueue(MainActivity.this).add(stringRequest);

    }

    private void getOrders2 (String key, String mac){
        //progressBar.setVisibility(View.VISIBLE);
        String BASE_URL2;
        BASE_URL2=BASE_URL+"?key="+key+"&mac="+mac+"&opt=tot";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        try {

                            String buff=getResources().getString(R.string.no_order)+"!";
                            String buff2;
                            int i;
                            JSONArray array = new JSONArray(response);
                            for (i = 0; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);

                                //int id = object.getInt("id");
                                //String lat = object.getString("lat");
                                //String lon = object.getString("lon");
                                //String nume = object.getString("nume");
                                //String tel = object.getString("tel");
                                //String adresa = object.getString("adresa");
                                String data_start = object.getString("data_start");
                                String data_comanda = object.getString("data_comanda");
                                String data_client = object.getString("data_client");
                                String nr_masina = object.getString("nr_masina");
                                Integer status = object.getInt("status");
                                String obs = object.getString("obs");
                                String obs_sofer = object.getString("obs_sofer");
                                String obs_sofer1 = object.getString("obs_sofer1");
                                //String link = object.getString("link");
                                if(obs_sofer.equals("null"))
                                    obs_sofer="";
                                if(obs_sofer1.equals("null"))
                                    obs_sofer1="";

                                if(flag != status) {
                                    //Tv_titlu.setText(R.string.update_titlu);
                                    //updateUIValues3(lat, lon, adresa, nume, tel);
                                    createNotificationChannel();

                                    buff2 = data_comanda + "\n" + getResources().getString(R.string.order_received) + ". (" + obs + ")\n\n";
                                    buff=buff2;
                                    if (status > 0) {
                                        buff2 = data_start + "\n" + getResources().getString(R.string.car_on_way) + ". (" + nr_masina + ")\n"+obs_sofer+"\n\n";
                                        buff = buff + buff2;
                                        //notifyThis("Taxi", buff2, status);
                                    }
                                    if (status > 1) {
                                        buff2 = data_client + "\n" + getResources().getString(R.string.car_arrived) + "!\n"+obs_sofer1+"\n";
                                        buff = buff + buff2;
                                        //notifyThis("Taxi", buff2, status);
                                    }
                                    notifyThis("Taxi", buff2, status);
                                    Tv_status.setText(buff);
                                    flag=status;
                                }


                            }
                            if(i==0){
                            Tv_status.setText(buff);}
                        }catch (Exception e){
                            //Tv_status.setText("catch"+e.getMessage());
                            //Toast.makeText(MainActivity.this, response,Toast.LENGTH_LONG).show();

                        }

                        //mAdapter = new RecyclerAdapter(MainActivity.this,products);
                        //recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //progressBar.setVisibility(View.GONE);
                //Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);

    }

    private void getRate (String key, String mac){
        //progressBar.setVisibility(View.VISIBLE);
        String BASE_URL2;
        BASE_URL2=BASE_URL+"?key="+key+"&mac="+mac+"&opt=rate";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        try {
                            String buff=getResources().getString(R.string.tv_labelRatingBar2);
                            SpannableString spannableString = new SpannableString(buff);
                            ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);

                            RatingBar.setEnabled(false);
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                String buff2="",nota1="";
                                last_order_date = object.getString("data_stop");
                                String nota = object.getString("nota");
                                if(nota.equals("null")){
                                    buff2=getResources().getString(R.string.tv_labelRatingBar)+": "+last_order_date;
                                    buff=buff2;
                                }else{
                                    //nota=Float.parseFloat(m_Text)*10;
                                    //nota1=(int)(nota + 0.5);
                                    //m_Text=nota1.toString();
                                    nota1=nota.substring(0,1)+"."+nota.substring(1,2);
                                    buff2=getResources().getString(R.string.tv_labelRatingBar)+": "+last_order_date;
                                    buff=buff2+" ("+nota1+")";
                                }

                                spannableString = new SpannableString(buff);
                                spannableString.setSpan(red,buff2.length(), buff.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                //Tv_labelRatingBar.setText(spannableString);
                                RatingBar.setEnabled(true);


                            }
                            if(!RatingBar.isEnabled())
                            {Tv_ratingBar.setText("0");
                                RatingBar.setRating(0);
                            }

                            Tv_labelRatingBar.setText(spannableString);
                        }catch (Exception e){
                            //Tv_status.setText("catch"+e.getMessage());
                            //Toast.makeText(MainActivity.this, response,Toast.LENGTH_LONG).show();

                        }

                        //mAdapter = new RecyclerAdapter(MainActivity.this,products);
                        //recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //progressBar.setVisibility(View.GONE);
                //Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);

    }

    //lat, lon, nume, tel, mac, adresa, link, obs, key
    private void sendOrders(String key, String mac, String nume, String tel, String lat, String lon, String adresa, String link, String obs){
        //s = e1.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsend,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response.trim(),Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","send");
                params.put("key",key);
                params.put("mac",mac);
                params.put("lon",lon);
                params.put("lat",lat);
                params.put("nume",nume);
                params.put("tel",tel);
                params.put("adresa",adresa);
                params.put("link",link);
                params.put("obs",obs);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    private void deleteOrders(String key, String mac){
        //s = e1.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsend,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response.trim(),Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","del");
                params.put("key",key);
                params.put("mac",mac);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }
    private void rateOrders(String key, String mac, String nota){
        //s = e1.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsend,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response.trim(),Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","rate");
                params.put("key",key);
                params.put("mac",mac);
                params.put("nota",nota);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    public SSLSocketFactory getSocketFactory(Context context)
            throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        // Load CAs from an InputStream (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        InputStream caInput = new BufferedInputStream(context.getResources().openRawResource(R.raw.androidtaxiineuroca));
        //InputStream caInput = new BufferedInputStream(MainActivity.this.getResources().openRawResource(R.raw.androidtaxiineuroca123));
        // I paste my myFile.crt in raw folder under res.
        Certificate ca;

        //noinspection TryFinallyCanBeTryWithResources
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext.getSocketFactory();
    }



    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";

        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                                return myTrustedAnchors;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
    }//NukeSSLCerts

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            CharSequence name = "TaxiIneu_channel";
            String description = "TaxiIneu_Canal_notificare";
            //int importance = NotificationManager.IMPORTANCE_DEFAULT;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            String CHANNEL_ID="TaxiIneu_CN_Id";//TaxiIneu_CN_Id2
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(16776960);//galben
            //channel.setLightColor(16711680);//red
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void deleteNotificationChannel(String id){
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        //String id = "my_channel_01";
        String buff="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //NotificationChannel mChannel = mNotificationManager.getNotificationChannel(id);
            mNotificationManager.deleteNotificationChannel(id);
            //buff=mChannel.
            //Toast.makeText(MainActivity.this,buff,Toast.LENGTH_LONG).show();
            /*String id = "my_channel_01"; daca se stie id
            mNotificationManager.deleteNotificationChannel(id);
            */
        }
    }

    public void notifyThis(String title, String message, Integer id) {
        /*
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        */

        String CHANNEL_ID="TaxiIneu_CN_Id";
        NotificationCompat.Builder b = new NotificationCompat.Builder(this,CHANNEL_ID);
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_baseline_local_taxi_24)
                .setColor(Color.rgb(255,0,0))
                .setTicker("www{your tiny message}")
                .setContentTitle(title)
                .setContentText(message)
                //.setContentInfo("INFO");
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message));



        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, b.build());

    }


}//main activity

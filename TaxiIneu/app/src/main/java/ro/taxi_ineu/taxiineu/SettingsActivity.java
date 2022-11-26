package ro.taxi_ineu.taxiineu;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceFragmentCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private Button SaveButton;
    private EditText NumeText;
    private EditText TelText;
    private Button LocationButton,SaveButton2,AdrButton;
    private SwitchCompat Switch1;
    public static final int Deafult_update_interval = 10;//30
    public static final int Fast_update_interval = 5;
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    public static final int PRIORITY_LOW_POWER = 104;
    public static final int PRIORITY_NO_POWER = 105;
    private static final int PERMISSION_FINE_LOCATION = 99;
    private TextView Tv_altitude, Tv_accuracy, Maplink, Tv_status,TextSetari;
    private TextView Tv_lat, Tv_lon, Tv_address;
    private RadioGroup Radiog;
    private RadioButton RadioButton1, RadioButton2, RadioButton3;
    double latitude_glob = 0;
    double longitude_glob = 0;
    private String link, nume, tel;


    //variabile to remember if we are tracking location or not
    private boolean updateOn = false;

    //Google's API for location services
    FusedLocationProviderClient fusedLocationProviderClient;

    //Location request is a config file for all settings related to FusedLocationProviderClient
    private LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        NumeText = findViewById(R.id.editTextNume);
        TelText = findViewById(R.id.editTextTel);
        SaveButton = findViewById(R.id.saveButton);
        LocationButton = findViewById(R.id.locationButton);
        SaveButton2 = findViewById(R.id.saveButton2);
        AdrButton = findViewById(R.id.adrButton);
        Switch1 = findViewById(R.id.switch1);

        Tv_lat = findViewById(R.id.tv_lat);
        Tv_lon = findViewById(R.id.tv_lon);
        Tv_altitude = findViewById(R.id.tv_altitude);
        Tv_accuracy = findViewById(R.id.tv_accuracy);
        Tv_address = findViewById(R.id.tv_address);
        TextSetari=findViewById(R.id.textSetari);
        Maplink = findViewById(R.id.maplink);
        Tv_status= findViewById(R.id.tv_status);
        Radiog = findViewById(R.id.radioGroup);
        RadioButton1=findViewById(R.id.radioButton1);
        RadioButton2=findViewById(R.id.radioButton2);
        RadioButton3=findViewById(R.id.radioButton3);


        // Set all properties of LocationRequest
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * Deafult_update_interval);//5000
        locationRequest.setFastestInterval(1000 * Fast_update_interval);//2000
        locationRequest.setPriority(PRIORITY_HIGH_ACCURACY);
        //locationRequest.setPriority(PRIORITY_BALANCED_POWER_ACCURACY);



        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savefunc();

            }
        });

        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCurrentLocation();
                Tv_status.setText(R.string.locationButton);
                //sendProducts();
            }
        });
        SaveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Tv_status.setText("Save2");
                //Tv_status.setText("There is no need for wrapping in a ScrollView. Easily the best and most appropriate answer, especially considering that EditText is a TextView subclass and the question is how to make the textview scrollable, not how to work around the problem");

                if(RadioButton1.isChecked()==true) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    // write all the data entered by the user in SharedPreference and apply
                    myEdit.putString("latitude1", Tv_lat.getText().toString());
                    myEdit.putString("longitude1", Tv_lon.getText().toString());
                    myEdit.putString("adresa1", Tv_address.getText().toString());
                    //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
                    myEdit.apply();
                    String buff="";
                    buff=getResources().getString(R.string.saveButton)+" " +getResources().getString(R.string.radioButton1);
                    Toast.makeText(SettingsActivity.this, buff, Toast.LENGTH_SHORT).show();
                }
                if(RadioButton2.isChecked()==true) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    // write all the data entered by the user in SharedPreference and apply
                    myEdit.putString("latitude2", Tv_lat.getText().toString());
                    myEdit.putString("longitude2", Tv_lon.getText().toString());
                    myEdit.putString("adresa2", Tv_address.getText().toString());
                    //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
                    myEdit.apply();
                    String buff="";
                    buff=getResources().getString(R.string.saveButton)+" " +getResources().getString(R.string.radioButton2);
                    Toast.makeText(SettingsActivity.this, buff, Toast.LENGTH_SHORT).show();
                }
                if(RadioButton3.isChecked()==true) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    // write all the data entered by the user in SharedPreference and apply
                    myEdit.putString("latitude3", Tv_lat.getText().toString());
                    myEdit.putString("longitude3", Tv_lon.getText().toString());
                    myEdit.putString("adresa3", Tv_address.getText().toString());
                    //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
                    myEdit.apply();
                    String buff="";
                    buff=getResources().getString(R.string.saveButton)+" " +getResources().getString(R.string.radioButton3);
                    Toast.makeText(SettingsActivity.this, buff, Toast.LENGTH_SHORT).show();
                }

            }
        });

        AdrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double latitude,longitude;
                latitude=Double.parseDouble(Tv_lat.getText().toString());
                longitude=Double.parseDouble(Tv_lon.getText().toString());
                getCurrentAddress(latitude, longitude);
                //Tv_status.setText("Adr");
                //sendProducts();
            }
        });


        Switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buff;
                Boolean status;
                status=Switch1.isChecked();
                if(status==true) {
                    Tv_lat.setTextColor(Color.RED);
                    Tv_lon.setTextColor(Color.RED);
                    Tv_address.setTextColor(Color.RED);

                }
                else  {
                    Tv_lat.setTextColor(Color.GRAY);
                    Tv_lon.setTextColor(Color.GRAY);
                    Tv_address.setTextColor(Color.GRAY);
                }

            }
        });

        Tv_lat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean status;
                status = Switch1.isChecked();
                if (status == true) {
                    String buff;
                    buff = Tv_lat.getText().toString();
                    //Tv_status.setText(buff);

                    //start popup
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setTitle(getResources().getString(R.string.tv_labellat));
                    builder.setCancelable(false);
                    final EditText input = new EditText(SettingsActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    builder.setView(input);

                    input.setText(buff);

                    builder.setNegativeButton(getResources().getString(R.string.pop_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.pop_save), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String m_Text, latitude, longitude;
                            m_Text = input.getText().toString();
                            Tv_lat.setText(m_Text);
                            latitude = m_Text;
                            longitude = Tv_lon.getText().toString();
                            link = "https://www.google.ro/maps/place/" + latitude + "+" + longitude;
                            Maplink.setText(link);
                            //Toast.makeText(SettingsActivity.this,"log_out_success"+m_Text, Toast.LENGTH_SHORT).show();

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                    //stop popup


                }//if
            }
        });
        Tv_lon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean status;
                status = Switch1.isChecked();
                if (status == true) {
                    String buff;
                    buff = Tv_lon.getText().toString();
                    //Tv_status.setText(buff);

                    //start popup
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setTitle(getResources().getString(R.string.tv_labellon));
                    builder.setCancelable(false);
                    final EditText input = new EditText(SettingsActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    builder.setView(input);

                    input.setText(buff);

                    builder.setNegativeButton(getResources().getString(R.string.pop_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.pop_save), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String m_Text, latitude, longitude;
                            m_Text = input.getText().toString();
                            Tv_lon.setText(m_Text);
                            longitude = m_Text;
                            latitude = Tv_lat.getText().toString();
                            link = "https://www.google.ro/maps/place/" + latitude + "+" + longitude;
                            Maplink.setText(link);
                            //Toast.makeText(SettingsActivity.this,"log_out_success"+m_Text, Toast.LENGTH_SHORT).show();

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                    //stop popup

                }//if
            }
        });

        Tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean status;
                status = Switch1.isChecked();
                if (status == true) {
                    String buff;
                    buff = Tv_address.getText().toString();
                    //Tv_status.setText("adr"+buff);

                    //start popup
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    //builder.setMessage(getResources().getString(R.string.do_you_really_want_to_signout));
                    //builder.setMessage("Adresa");
                    builder.setTitle(getResources().getString(R.string.tv_lbladdress));
                    builder.setCancelable(false);
                    final EditText input = new EditText(SettingsActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    builder.setView(input);

                    input.setText(buff);

                    builder.setNegativeButton(getResources().getString(R.string.pop_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.pop_save), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String m_Text;
                            m_Text = input.getText().toString();
                            Tv_address.setText(m_Text);
                            //Toast.makeText(SettingsActivity.this,"log_out_success"+m_Text, Toast.LENGTH_SHORT).show();

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                    //stop popup

                }//if
            }
        });

        Radiog.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Switch1.isChecked() == false) {
                    String s1, s2, s3;
                    //buff=String.valueOf(checkedId);
                    //Tv_status.setText(buff);
                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    switch (checkedId) {
                        case R.id.radioButton1:
                            TextSetari.setText(R.string.radioButton1);
                            s1 = sh.getString("latitude1", "");
                            s2 = sh.getString("longitude1", "");
                            s3 = sh.getString("adresa1", "");
                            Tv_lat.setText(s1);
                            Tv_lon.setText(s2);
                            Tv_address.setText(s3);
                            link = "https://www.google.ro/maps/place/" + s1 + "+" + s2;
                            Maplink.setText(link);
                            //age.setText(String.valueOf(a));
                            break;
                        case R.id.radioButton2:
                            TextSetari.setText(R.string.radioButton2);
                            s1 = sh.getString("latitude2", "");
                            s2 = sh.getString("longitude2", "");
                            s3 = sh.getString("adresa2", "");
                            Tv_lat.setText(s1);
                            Tv_lon.setText(s2);
                            Tv_address.setText(s3);
                            link = "https://www.google.ro/maps/place/" + s1 + "+" + s2;
                            Maplink.setText(link);
                            break;
                        case R.id.radioButton3:
                            TextSetari.setText(R.string.radioButton3);
                            s1 = sh.getString("latitude3", "");
                            s2 = sh.getString("longitude3", "");
                            s3 = sh.getString("adresa3", "");
                            Tv_lat.setText(s1);
                            Tv_lon.setText(s2);
                            Tv_address.setText(s3);
                            link = "https://www.google.ro/maps/place/" + s1 + "+" + s2;
                            Maplink.setText(link);
                            break;
                    }
                }
            }

        });

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }//on create

    // Fetch the stored data in onResume()
    // Because this is what will be called
    // when the app opens again
    @Override
    protected void onResume() {
        super.onResume();

        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String s1 = sh.getString("nume", "");
        String s2 = sh.getString("tel", "");
        //int a = sh.getInt("age", 0);

        // Setting the fetched data
        // in the EditTexts
        NumeText.setText(s1);
        TelText.setText(s2);
        //age.setText(String.valueOf(a));
    }
    private void savefunc() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("nume", NumeText.getText().toString());
        myEdit.putString("tel", TelText.getText().toString());
        //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
        myEdit.apply();
        String buff="";
        buff=getResources().getString(R.string.saveButton)+" " +getResources().getString(R.string.textNume)+" + "+getResources().getString(R.string.textTel);
        Toast.makeText(SettingsActivity.this, buff, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_settings,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_map:
                Intent intent = new Intent(SettingsActivity.this, MapActivity.class);
                startActivity(intent);
                Toast.makeText(SettingsActivity.this, R.string.action_map, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_help:
                Intent intent1 = new Intent(SettingsActivity.this, HelpActivity.class);
                startActivity(intent1);
                Toast.makeText(SettingsActivity.this, R.string.action_help, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            //setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
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

        Tv_lat.setText(String.valueOf(latitude));
        Tv_lon.setText(String.valueOf(longitude));

        link="https://www.google.ro/maps/place/"+latitude+"+"+longitude;
        Maplink.setText(link);

        Tv_accuracy.setText(String.valueOf(accuracy));

        Tv_altitude.setText(altitude);


    }
    private void getCurrentAddress(Double latitude, Double longitude) {
        Geocoder geocoder = new Geocoder(SettingsActivity.this);
        try {
            List<Address> adresses= geocoder.getFromLocation(latitude, longitude,1);
            Tv_address.setText(adresses.get(0).getAddressLine(0));
            //Tv_status.setText(adresses.get(0).getAddressLine(0));
        }
        catch (Exception e) {
            Tv_address.setText(R.string.no_address);
        }

    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(SettingsActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(SettingsActivity.this)
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
                    Toast.makeText(SettingsActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(SettingsActivity.this, 2);
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
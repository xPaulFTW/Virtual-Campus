package ro.taxi_ineu.taxiineusofer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class OrderDetailActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private static  final String BASE_URL = "https://android.taxi-ineu.ro/getSofer.php";
    private static  final String urlsend = "https://android.taxi-ineu.ro/sendSofer.php";
    private String nr_masina_global;
    private Boolean login_global;
    private String orderId_global;

    private TextView Tv_lat,Tv_lon,Tv_nume,Tv_phone,Maplink,Tv_obs,Tv_address,Tv_order;
    private Button ButtonStart,ButtonClient,ButtonStop;
    private SwitchCompat Switch1, Switch2;
    private EditText EditTextObs1,EditTextObs2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        orderId_global = intent.getStringExtra("order_id");
        refreshUIValues(orderId_global);
        //Toast.makeText(OrderDetailActivity.this,orderId_global,Toast.LENGTH_SHORT).show();
        String buff="";
        progressBar=findViewById(R.id.progressBar);
        //Spinner_car.setOnItemSelectedListener(this);
        OrdersActivity.NukeSSLCerts.nuke();
        progressBar.setVisibility(View.GONE);

        //tv_lat,tv_lon,tv_nume,tv_phone,maplink,tv_obs,tv_address;
        Tv_lat = findViewById(R.id.tv_lat);
        Tv_lon = findViewById(R.id.tv_lon);
        Tv_address = findViewById(R.id.tv_address);
        Tv_nume=findViewById(R.id.tv_nume);
        Tv_phone=findViewById(R.id.tv_phone);
        Maplink = findViewById(R.id.maplink);
        Tv_obs= findViewById(R.id.tv_obs);
        Tv_order= findViewById(R.id.tv_order);
        ButtonStart= findViewById(R.id.buttonStart);
        ButtonClient= findViewById(R.id.buttonClient);
        ButtonStop= findViewById(R.id.buttonStop);
        Switch1= findViewById(R.id.switch1);
        Switch2= findViewById(R.id.switch2);
        EditTextObs1= findViewById(R.id.editTextObs1);
        EditTextObs2= findViewById(R.id.editTextObs2);

        ActionBar actionBar = getSupportActionBar();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        nr_masina_global = sh.getString("nr_masina", "");

        EditTextObs1.setEnabled(false);
        EditTextObs2.setEnabled(false);

        if (actionBar != null) {
            //buff=getResources().getString(R.string.orderstitle)+" "+nr_masina_global;
            buff="#"+sh.getString("user", "")+"   "+nr_masina_global;
            actionBar.setTitle(buff);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key,auth_key,user,pass,nr_masina,obs_sofer;
                //Toast.makeText(OrderDetailActivity.this,"Start", Toast.LENGTH_LONG).show();

                key= UUID.randomUUID().toString();
                auth_key=md5(key+"TaxiIneu");
                user = sh.getString("user", "");
                pass = sh.getString("pass", "");
                nr_masina = sh.getString("nr_masina", "");
                //sendLogout(key, auth_key, user, md5(pass));
                obs_sofer = EditTextObs1.getText().toString();
                sendStart(key, auth_key, user, md5(pass), orderId_global, nr_masina, obs_sofer);


            }
        });
        ButtonClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(OrderDetailActivity.this,"Client", Toast.LENGTH_LONG).show();
                String key,auth_key,user,pass,nr_masina,obs_sofer1;
                key= UUID.randomUUID().toString();
                auth_key=md5(key+"TaxiIneu");
                user = sh.getString("user", "");
                pass = sh.getString("pass", "");
                obs_sofer1 = EditTextObs2.getText().toString();
                sendClient(key, auth_key, user, md5(pass), orderId_global, obs_sofer1);

            }
        });
        ButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(OrderDetailActivity.this,"Stop", Toast.LENGTH_LONG).show();
                String key,auth_key,user,pass,nr_masina,obs_sofer1;
                key= UUID.randomUUID().toString();
                auth_key=md5(key+"TaxiIneu");
                user = sh.getString("user", "");
                pass = sh.getString("pass", "");
                obs_sofer1 = EditTextObs2.getText().toString();
                sendStop(key, auth_key, user, md5(pass), orderId_global);

            }
        });

        Switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buff;
                Boolean status;
                status=Switch1.isChecked();
                if(status==true) {
                    EditTextObs1.setEnabled(true);

                }
                else  {
                    EditTextObs1.setEnabled(false);
                }

            }
        });

        Switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buff;
                Boolean status;
                status=Switch2.isChecked();
                if(status==true) {
                    EditTextObs2.setEnabled(true);

                }
                else  {
                    EditTextObs2.setEnabled(false);
                }

            }
        });

    }//oncreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_order_detail,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String key,auth_key,user,pass;
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        user = sh.getString("user", "");
        pass = sh.getString("pass", "");

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_logout:

                myEdit.putBoolean("login",false);
                myEdit.putString("nr_masina", "");
                myEdit.apply();

                key= UUID.randomUUID().toString();
                auth_key=md5(key+"TaxiIneu");
                sendLogout(key, auth_key, user, md5(pass));
                Toast.makeText(OrderDetailActivity.this, R.string.action_logout, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_refresh:
                //key= UUID.randomUUID().toString();
                //auth_key=md5(key+"TaxiIneu");
                //getOrders(key, auth_key, user, md5(pass));
                //getOrderdetail(String key, String auth_key, String id, String parola, String order_id)
                refreshUIValues(orderId_global);
                Toast.makeText(OrderDetailActivity.this, R.string.action_refresh, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
    private void refreshUIValues (String order_id) {
        String key,auth_key,user,pass;
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        user = sh.getString("user", "");
        pass = sh.getString("pass", "");

        key= UUID.randomUUID().toString();
        auth_key=md5(key+"TaxiIneu");
        getOrderdetail(key, auth_key, user, md5(pass), order_id);

    }
    private void updateUIValues (String latitude, String longitude, String address, String nume, String tel, String maplink, String obs, String order_id) {
        //update all of the text view objects with a new location
        Tv_lat.setText(String.valueOf(latitude));
        Tv_lon.setText(String.valueOf(longitude));

        Tv_nume.setText(nume);
        Tv_phone.setText(tel);

        Maplink.setText(maplink);

        Tv_obs.setText(obs);
        Tv_address.setText(address);
        Tv_order.setText(order_id);
    }

    private void sendLogout(String key, String auth_key, String id, String parola){
        //s = e1.getText().toString();
        final String buff="",pass_ok,pass_bad,bad_user;
        pass_ok=md5("pass_ok"+key);
        pass_bad=md5("pass_bad"+key);
        bad_user=md5("bad_user"+key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsend,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals(pass_ok)){
                            OrderDetailActivity.this.finish();
                            Intent intent = new Intent("finish_activity");
                            sendBroadcast(intent);
                            //onBackPressed();
                            //Toast.makeText(MainActivity.this,"pass_ok",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(pass_bad)){
                            Toast.makeText(OrderDetailActivity.this,"pass_bad",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(bad_user)){
                            Toast.makeText(OrderDetailActivity.this,"bad_user",Toast.LENGTH_LONG).show();
                        }else {
                            //System.out.println("buff:" + response.trim());
                            Toast.makeText(OrderDetailActivity.this,response.trim(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderDetailActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","logout");
                params.put("key",key);
                params.put("auth_key",auth_key);
                params.put("id",id);
                params.put("parola",parola);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OrderDetailActivity.this);
        requestQueue.add(stringRequest);

    }

    private void getOrderdetail(String key, String auth_key, String id, String parola, String order_id){
        //s = e1.getText().toString();
        final String buff="",pass_ok,pass_bad,bad_user;
        //pass_ok=md5("pass_ok"+key);
        pass_bad=md5("pass_bad"+key);
        bad_user=md5("bad_user"+key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsend,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.trim().equals(pass_bad)){
                            Toast.makeText(OrderDetailActivity.this,"pass_bad",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(bad_user)){
                            Toast.makeText(OrderDetailActivity.this,"bad_user",Toast.LENGTH_LONG).show();
                        }else {


                            Boolean flag = false;
                            try {
                                String buff="";
                                //String buff=getResources().getString(R.string.no_order)+"!";

                                JSONArray array = new JSONArray(response);

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);

                                    //int id = object.getInt("id");
                                    Integer order_id = object.getInt("id");
                                    String nume = object.getString("nume");
                                    String tel = object.getString("tel");
                                    String mac = object.getString("mac");
                                    String lat = object.getString("lat");
                                    String lon = object.getString("lon");
                                    String adresa = object.getString("adresa");
                                    String link = object.getString("link");
                                    String status = object.getString("status");
                                    String obs = object.getString("obs");
                                    String CURRENT_TIME = object.getString("CURRENT_TIME");
                                    String minutes = object.getString("minutes");

                                    //buff=String.valueOf(id);
                                    updateUIValues (lat,lon, adresa, nume, tel, link, obs,String.valueOf(order_id));
                                    //buff=buff+id.toString()+" "+nr_masina2+"\n";
                                    //buff=buff+"id="+order_id.toString()+" "+mac+" "+adresa+"\n";
                                    flag = true;

                                }

                                //buff="Comenzi:\n"+buff;
                                //Toast.makeText(OrdersActivity.this, buff, Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                flag = true;
                                //Tv_status.setText("catch"+e.getMessage());
                                Toast.makeText(OrderDetailActivity.this, "catch" + e.getMessage(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();

                            }

                            if (!flag) {
                                //System.out.println("buff:" + response.trim());
                                Toast.makeText(OrderDetailActivity.this, response.trim(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderDetailActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","getorderdetail");
                params.put("key",key);
                params.put("auth_key",auth_key);
                params.put("id",id);
                params.put("order_id",order_id);
                params.put("parola",parola);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OrderDetailActivity.this);
        requestQueue.add(stringRequest);

    }

    private void sendStart(String key, String auth_key, String id, String parola, String order_id, String nr_masina, String obs_sofer){
        //s = e1.getText().toString();
        final String buff="",pass_ok,pass_bad,bad_user;
        pass_ok=md5("pass_ok"+key);
        pass_bad=md5("pass_bad"+key);
        bad_user=md5("bad_user"+key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsend,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals(pass_ok)){
                            //OrderDetailActivity.this.finish();
                            //Intent intent = new Intent("finish_activity");
                            //sendBroadcast(intent);
                            //onBackPressed();
                            Toast.makeText(OrderDetailActivity.this,"star_cursa",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(pass_bad)){
                            Toast.makeText(OrderDetailActivity.this,"pass_bad",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(bad_user)){
                            Toast.makeText(OrderDetailActivity.this,"bad_user",Toast.LENGTH_LONG).show();
                        }else {
                            //System.out.println("buff:" + response.trim());
                            Toast.makeText(OrderDetailActivity.this,response.trim(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderDetailActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","start");
                params.put("key",key);
                params.put("auth_key",auth_key);
                params.put("id",id);
                params.put("parola",parola);
                params.put("order_id",order_id);
                params.put("nr_masina",nr_masina);
                params.put("obs_sofer",obs_sofer);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OrderDetailActivity.this);
        requestQueue.add(stringRequest);

    }

    private void sendClient(String key, String auth_key, String id, String parola, String order_id, String obs_sofer1){
        //s = e1.getText().toString();
        final String buff="",pass_ok,pass_bad,bad_user;
        pass_ok=md5("pass_ok"+key);
        pass_bad=md5("pass_bad"+key);
        bad_user=md5("bad_user"+key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsend,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals(pass_ok)){
                            //OrderDetailActivity.this.finish();
                            //Intent intent = new Intent("finish_activity");
                            //sendBroadcast(intent);
                            //onBackPressed();
                            Toast.makeText(OrderDetailActivity.this,"client",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(pass_bad)){
                            Toast.makeText(OrderDetailActivity.this,"pass_bad",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(bad_user)){
                            Toast.makeText(OrderDetailActivity.this,"bad_user",Toast.LENGTH_LONG).show();
                        }else {
                            //System.out.println("buff:" + response.trim());
                            Toast.makeText(OrderDetailActivity.this,response.trim(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderDetailActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","client");
                params.put("key",key);
                params.put("auth_key",auth_key);
                params.put("id",id);
                params.put("parola",parola);
                params.put("order_id",order_id);
                params.put("obs_sofer1",obs_sofer1);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OrderDetailActivity.this);
        requestQueue.add(stringRequest);

    }

    private void sendStop(String key, String auth_key, String id, String parola, String order_id){
        //s = e1.getText().toString();
        final String buff="",pass_ok,pass_bad,bad_user,comanda_in_derulare;
        pass_ok=md5("pass_ok"+key);
        pass_bad=md5("pass_bad"+key);
        bad_user=md5("bad_user"+key);
        comanda_in_derulare=md5("comanda_in_derulare"+key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsend,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals(pass_ok)){
                            //OrderDetailActivity.this.finish();
                            //Intent intent = new Intent("finish_activity");
                            //sendBroadcast(intent);
                            //onBackPressed();
                            Toast.makeText(OrderDetailActivity.this,"stop_delete",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(comanda_in_derulare)) {
                            Toast.makeText(OrderDetailActivity.this,"comanda_in_derulare",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(pass_bad)){
                            Toast.makeText(OrderDetailActivity.this,"pass_bad",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(bad_user)){
                            Toast.makeText(OrderDetailActivity.this,"bad_user",Toast.LENGTH_LONG).show();
                        }else {
                            //System.out.println("buff:" + response.trim());
                            Toast.makeText(OrderDetailActivity.this,response.trim(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderDetailActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","stop");
                params.put("key",key);
                params.put("auth_key",auth_key);
                params.put("id",id);
                params.put("parola",parola);
                params.put("order_id",order_id);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OrderDetailActivity.this);
        requestQueue.add(stringRequest);

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

}
package ro.taxi_ineu.taxiineusofer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {


    private Button ButtonLogin,ButtonCar;
    private CheckBox SaveLogin;
    private EditText EditTextUser;
    private TextInputEditText EditInputPassword;
    private Group Group_select_car;
    private Spinner Spinner_car;

    //php
    //private List<Product> products;
    private ProgressBar progressBar;
    private static  final String BASE_URL = "https://android.taxi-ineu.ro/getSofer.php";
    private static  final String urlsend = "https://android.taxi-ineu.ro/sendSofer.php";
    private String nr_masina_global;
    private Boolean login_global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButtonLogin = findViewById(R.id.buttonLogin);
        ButtonCar = findViewById(R.id.buttonCar);
        SaveLogin = findViewById(R.id.saveLogin);
        EditTextUser = findViewById(R.id.editTextUser);
        EditInputPassword = findViewById(R.id.editInputPassword);
        Group_select_car = findViewById(R.id.group_select_car);
        Spinner_car=findViewById(R.id.spinner_car);
        progressBar=findViewById(R.id.progressBar);
        //Spinner_car.setOnItemSelectedListener(this);
        NukeSSLCerts.nuke();
        progressBar.setVisibility(View.GONE);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.logintitle);
        }
        update_user_pass();


        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user,pass, buff="",key,auth_key;
                Boolean save_pass;
                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();

                save_pass=sh.getBoolean("save_pass",false);
                user=EditTextUser.getText().toString();
                if(save_pass){
                    pass=EditInputPassword.getText().toString();
                    if(pass.equals("****")) {
                        pass = sh.getString("pass", "");
                    }
                }else{
                    pass=EditInputPassword.getText().toString();
                }



                //save user and pass
                if(SaveLogin.isChecked()) {
                    myEdit.putBoolean("save_pass",true);
                    myEdit.putString("user", user);
                    myEdit.putString("pass", pass);
                    myEdit.apply();
                }else{
                    myEdit.putBoolean("save_pass",false);
                    myEdit.putString("user", user);
                    myEdit.putString("pass", pass);
                    myEdit.apply();
                }


                key=UUID.randomUUID().toString();
                auth_key=md5(key+"TaxiIneu");
                //System.out.println("key:" + key);
                sendLogin(key,auth_key, user, md5(pass));

                //buff="gata";
                //Toast.makeText(MainActivity.this,buff, Toast.LENGTH_SHORT).show();

            }
        });

        ButtonCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user,pass, buff="",key,auth_key,nr_masina;
                Long nr_id;
                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                nr_id = Spinner_car.getSelectedItemId();
                if(nr_id!=0) {
                    nr_masina = Spinner_car.getSelectedItem().toString();
                    user = sh.getString("user", "");
                    pass = sh.getString("pass", "");
                    key=UUID.randomUUID().toString();
                    auth_key=md5(key+"TaxiIneu");
                    nr_masina_global=nr_masina;
                    sendCar(key, auth_key,user, md5(pass), nr_masina);
                }else{
                    buff=getResources().getString(R.string.tv_label_nr_masina)+"!";
                    Toast.makeText(MainActivity.this,buff, Toast.LENGTH_LONG).show();
                }



            }
        });


    }//oncreate

    void update_user_pass(){
        String user,pass, key, auth_key;
        Boolean save_pass,login;
        //user=EditTextUser.getText().toString();
        //pass=EditInputPassword.getText().toString();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        save_pass=sh.getBoolean("save_pass",false);
        login=sh.getBoolean("login",false);
        //buff=save_pass.toString();
        //Toast.makeText(MainActivity.this,buff, Toast.LENGTH_SHORT).show();
        Group_select_car.setVisibility(View.INVISIBLE);
        if(save_pass) {
            user = sh.getString("user", "");
            //pass = sh.getString("pass", "");
            pass="****";
            EditTextUser.setText(user);
            EditInputPassword.setText(pass);
            SaveLogin.setChecked(true);
        }
        if(login){
            user = sh.getString("user", "");
            pass = sh.getString("pass", "");
            key=UUID.randomUUID().toString();
            auth_key=md5(key+"TaxiIneu");
            autologin(key, auth_key, user, md5(pass));
        }
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

    //lat, lon, nume, tel, mac, adresa, link, obs, key
    private void sendLogin(String key, String auth_key, String id, String parola){
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
                            Group_select_car.setVisibility(View.VISIBLE);
                            String key,auth_key;
                            key=UUID.randomUUID().toString();
                            auth_key=md5(key+"TaxiIneu");
                            getCars(key, auth_key);
                            //Toast.makeText(MainActivity.this,"pass_ok",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(pass_bad)){
                            Toast.makeText(MainActivity.this,"pass_bad",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(bad_user)){
                            Toast.makeText(MainActivity.this,"bad_user",Toast.LENGTH_LONG).show();
                        }else {
                            //System.out.println("buff:" + response.trim());
                            Toast.makeText(MainActivity.this,response.trim(),Toast.LENGTH_LONG).show();
                        }
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
                params.put("opt","login");
                params.put("key",key);
                params.put("auth_key",auth_key);
                params.put("id",id);
                params.put("parola",parola);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    private void sendCar(String key, String auth_key, String id, String parola, String nr_masina){
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
                            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sh.edit();

                            myEdit.putBoolean("login",true);
                            myEdit.putString("nr_masina", nr_masina_global);
                            myEdit.apply();
                            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                            startActivity(intent);
                            Group_select_car.setVisibility(View.INVISIBLE);
                            //Toast.makeText(MainActivity.this,"pass_ok",Toast.LENGTH_LONG).show();

                        }else if(response.trim().equals(pass_bad)){
                            Toast.makeText(MainActivity.this,"pass_bad",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(bad_user)){
                            Toast.makeText(MainActivity.this,"bad_user",Toast.LENGTH_LONG).show();
                        }else {


                            Boolean flag = false;
                            try {
                                String buff="";
                                //String buff=getResources().getString(R.string.no_order)+"!";
                                JSONArray array = new JSONArray(response);

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);

                                    //int id = object.getInt("id");
                                    Integer id_sofer = object.getInt("id");
                                    String nume = object.getString("nume");
                                    String prenume = object.getString("prenume");
                                    String nr_masina2 = object.getString("nr_masina");

                                    //buff=String.valueOf(id);
                                    //buff=buff+id.toString()+" "+nr_masina2+"\n";
                                    buff=buff+"id="+id_sofer.toString()+" "+nume+" "+prenume+"\n";
                                    flag = true;

                                }
                                buff=getResources().getString(R.string.LoginSameCar)+" "+nr_masina+":\n"+buff;
                                Toast.makeText(MainActivity.this, buff, Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                flag = true;
                                //Tv_status.setText("catch"+e.getMessage());
                                Toast.makeText(MainActivity.this, "catch" + e.getMessage(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();

                            }

                            if (!flag) {
                                //System.out.println("buff:" + response.trim());
                                Toast.makeText(MainActivity.this, response.trim(), Toast.LENGTH_LONG).show();
                            }
                        }
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
                params.put("opt","sendcar");
                params.put("key",key);
                params.put("auth_key",auth_key);
                params.put("id",id);
                params.put("parola",parola);
                params.put("nr_masina",nr_masina);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    private void getCars(String key, String auth_key){
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsend,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        String buff="";
                        try {

                            //String buff=getResources().getString(R.string.no_order)+"!";
                            //String[] masini = { "India", "USA", "China", "Japan", "Other"};
                            List<String> masini = new ArrayList<String>();
                            JSONArray array = new JSONArray(response);
                            masini.add(getResources().getString(R.string.select_car));
                            for (int i = 0; i<array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);

                                //int id = object.getInt("id");
                                String nr_masina = object.getString("numar");
                                Integer id = object.getInt("id");
                                masini.add(nr_masina);
                                //buff=String.valueOf(id);
                                //buff=buff+id.toString()+" "+nr_masina+"\n";
                            }

                            //String[] strings = new String[masini.size()];
                            //strings = masini.toArray(strings);//now strings is the resulting array

                            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, masini);
                            //ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, masini);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                            Spinner_car.setAdapter(spinnerArrayAdapter);

                            //Toast.makeText(MainActivity.this, buff,Toast.LENGTH_LONG).show();

                        }catch (Exception e){
                            //Tv_status.setText("catch"+e.getMessage());
                                Toast.makeText(MainActivity.this, "catch"+e.getMessage(),Toast.LENGTH_LONG).show();
                            //Toast.makeText(MainActivity.this, response,Toast.LENGTH_LONG).show();

                        }

                        //mAdapter = new RecyclerAdapter(MainActivity.this,products);
                        //recyclerView.setAdapter(mAdapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","getcar");
                params.put("key",key);
                params.put("auth_key",auth_key);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    private void autologin(String key, String auth_key, String id, String parola){
        progressBar.setVisibility(View.VISIBLE);
        final String buff="",pass_ok,pass_bad,bad_user;
        pass_ok=md5("pass_ok"+key);
        pass_bad=md5("pass_bad"+key);
        bad_user=md5("bad_user"+key);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsend,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.trim().equals(pass_bad)) {
                            Toast.makeText(MainActivity.this, "pass_bad", Toast.LENGTH_LONG).show();
                        } else if (response.trim().equals(bad_user)) {
                            Toast.makeText(MainActivity.this, "bad_user", Toast.LENGTH_LONG).show();
                        } else {

                            Boolean flag = false;
                            try {

                                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sh.edit();
                                //String buff=getResources().getString(R.string.no_order)+"!";
                                JSONArray array = new JSONArray(response);

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);

                                    //int id = object.getInt("id");
                                    //Integer id_sofer = object.getInt("id");
                                    //String nume = object.getString("nume");
                                    //String prenume = object.getString("prenume");
                                    String nr_masina = object.getString("nr_masina");
                                    Integer login = object.getInt("login");
                                    String pass_ok2 = object.getString("pass_ok");
                                    Boolean login_android=sh.getBoolean("login",false);
                                    //buff=String.valueOf(id);
                                    //buff=buff+id.toString()+" "+nr_masina+"\n";
                                    flag = true;
                                    if(pass_ok2.equals(pass_ok)) {
                                        if (login == 1 && login_android && !nr_masina.equals("null")) {
                                            myEdit.putString("nr_masina", nr_masina);
                                            myEdit.apply();
                                            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                                            startActivity(intent);
                                            Group_select_car.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                flag = true;
                                //Tv_status.setText("catch"+e.getMessage());
                                Toast.makeText(MainActivity.this, "catch" + e.getMessage(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();

                            }

                            if (!flag) {
                                //System.out.println("buff:" + response.trim());
                                Toast.makeText(MainActivity.this, response.trim(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }//response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","autologin");
                params.put("key",key);
                params.put("auth_key",auth_key);
                params.put("id",id);
                params.put("parola",parola);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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
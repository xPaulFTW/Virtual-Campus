package com.example.myloginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


//php
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

public class SecondActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name,email,orgunit,status;
    Button signOutBtn;
    private ProgressBar progressBar;
    private static  final String BASE_URL = "https://campus.lmvineu.ro/google_login/getBilet.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button SendButton, DeleteButton;
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        status = findViewById(R.id.status);
        signOutBtn = findViewById(R.id.signout);
        orgunit=findViewById(R.id.orgunit);
        progressBar = findViewById(R.id.progressBar);

        SendButton = findViewById(R.id.sendButton);
        DeleteButton = findViewById(R.id.deleteButton);
        //products = new ArrayList<>();
        progressBar.setVisibility(View.GONE);

        //merge accepta toate certificatele si serverele
        NukeSSLCerts.nuke();

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String personName =acct.getDisplayName();
            String personEmail =acct.getEmail();
            String personOrgUnit =acct.getId();
            name.setText(personName);
            email.setText(personEmail);
            orgunit.setText(personOrgUnit);
        }
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume, mac;
                //mac="102359356829445306841";
                mac=acct.getId();
                getOrders (mac);

                Toast.makeText(SecondActivity.this,"Get", Toast.LENGTH_SHORT).show();
            }
        });

        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("");
                Toast.makeText(SecondActivity.this,"Delete", Toast.LENGTH_SHORT).show();
            }
        });



    }//on create


    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
             finish();
             startActivity(new Intent(SecondActivity.this,MainActivity.class));
            }
        });
    }


    private void getOrders (String mac){
        progressBar.setVisibility(View.VISIBLE);
        String BASE_URL2;
       // ?opt=tot&mac=102359356829445306841
        BASE_URL2=BASE_URL+"?opt=tot&mac="+mac;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        try {

                            //String buff=getResources().getString(R.string.no_order)+"!";
                            String buff = "";
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                //id	email	Nume	Clasa	Orgunit	Motiv	Obs	data1	data2	ora1	ora2	id_google	valid
                                String email = object.getString("email");
                                String Nume = object.getString("Nume");
                                String Clasa = object.getString("Clasa");
                                String Orgunit = object.getString("Orgunit");
                                String Motiv = object.getString("Motiv");
                                String Obs = object.getString("Obs");
                                String data1 = object.getString("data1");
                                String data2 = object.getString("data2");
                                String ora1 = object.getString("ora1");
                                String ora2 = object.getString("ora2");
                                String id_google = object.getString("id_google");
                                String valid = object.getString("valid");

                                buff = buff + data1 + " " + ora1 + " " + data2 + " " + ora2 + " " + valid + " \n";
                                /*
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

                                //Tv_titlu.setText(R.string.update_titlu);
                                //updateUIValues3(lat, lon, adresa, nume, tel);
                                buff=data_comanda+"\n"+getResources().getString(R.string.order_received)+". ("+obs+")\n\n";

                                if(status>0) {
                                    buff = buff + data_start + "\n" + getResources().getString(R.string.car_on_way) + ". (" + nr_masina + ")\n"+obs_sofer+"\n\n";
                                }
                                if(status>1) {
                                    buff = buff + data_client + "\n" + getResources().getString(R.string.car_arrived) + "!\n"+obs_sofer1+"\n";
                                }

                            */
                            }
                            status.setText(buff);
                            //Tv_status.setText(buff);
                        }catch (Exception e){
                            //Tv_status.setText("catch"+e.getMessage());
                            Toast.makeText(SecondActivity.this, response,Toast.LENGTH_LONG).show();

                        }

                        //mAdapter = new RecyclerAdapter(MainActivity.this,products);
                        //recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(SecondActivity.this, error.toString(),Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(SecondActivity.this).add(stringRequest);

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

}
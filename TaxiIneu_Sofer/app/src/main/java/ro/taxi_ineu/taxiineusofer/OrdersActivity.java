package ro.taxi_ineu.taxiineusofer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//RecyclerView
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private static  final String BASE_URL = "https://android.taxi-ineu.ro/getSofer.php";
    private static  final String urlsend = "https://android.taxi-ineu.ro/sendSofer.php";
    private String nr_masina_global;
    private Boolean login_global;

    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private RecyclerTouchListener touchListener;

    List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
    String buff="";
        progressBar=findViewById(R.id.progressBar);
        //Spinner_car.setOnItemSelectedListener(this);
        OrdersActivity.NukeSSLCerts.nuke();
        progressBar.setVisibility(View.GONE);

        ActionBar actionBar = getSupportActionBar();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        nr_masina_global = sh.getString("nr_masina", "");
        if (actionBar != null) {
            //buff=getResources().getString(R.string.orderstitle)+" "+nr_masina_global;
            buff="#"+sh.getString("user", "")+"   "+nr_masina_global;
            actionBar.setTitle(buff);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //recyclerView
        recyclerView = findViewById(R.id.recyclerview);
        recyclerviewAdapter = new RecyclerviewAdapter(this);

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //System.out.println("ScrollView Disabled");
                //Toast.makeText(OrdersActivity.this, "ScrollView Disabled", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        /*
        Task task = new Task("Buy Dress","Buy Dress at Shoppershop for coming functions");
        taskList.add(task);
        task = new Task("Go For Walk","Wake up 6AM go for walking");
        taskList.add(task);
        task = new Task("Office Work","Complete the office works on Time");
        taskList.add(task);
        task = new Task("watch Repair","Give watch to service center");
        taskList.add(task);
        task = new Task("Recharge Mobile","Recharge for 10$ to my **** number");
        taskList.add(task);
        task = new Task("Read book","Read android book completely");
        taskList.add(task);
        task = new Task("Read book1","Read android book completely");
        taskList.add(task);
        task = new Task("Read book2","Read android book completely");
        taskList.add(task);
*/
        //recyclerviewAdapter.setTaskList(taskList);
        //recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new RecyclerTouchListener(this,recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        String buff;
                        buff=taskList.get(position).getId().toString();

                        Intent intent = new Intent(OrdersActivity.this,OrderDetailActivity.class);
                        
                        intent.putExtra("order_id",buff);
                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(),taskList.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        //Toast.makeText(getApplicationContext(),taskList.get(position).getId(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setSwipeOptionViews(R.id.delete_task,R.id.edit_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID){
                            case R.id.delete_task:
                                //taskList.remove(position);
                                String number = taskList.get(position).getPhone().toString();
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
                                //recyclerviewAdapter.setTaskList(taskList);
                                break;
                            case R.id.edit_task:
                                // Use format with "tel:" and phoneNumber created is
                                // stored in u.
                                Uri u = Uri.parse("tel:" + taskList.get(position).getPhone().toString());

                                // Create the intent and set the data for the
                                // intent as the phone number.
                                Intent i = new Intent(Intent.ACTION_DIAL, u);
                                //Intent i = new Intent(Intent.ACTION_CALL, u);

                                try
                                {
                                    // Launch the Phone app's dialer with a phone
                                    // number to dial a call.
                                    startActivity(i);
                                }
                                catch (SecurityException s)
                                {
                                    // show() method display the toast with
                                    // exception message.
                                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_LONG).show();
                                }

                                //Toast.makeText(getApplicationContext(),"Edit Not Available",Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                }).setSwipeableDreapta(new RecyclerTouchListener.OnSwipeListener2(){
                    @Override
                    public void onSwipeDreapta(int position) {
                        Toast.makeText(getApplicationContext(),taskList.get(position).getId().toString(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(),taskList.get(position).getId(), Toast.LENGTH_SHORT).show();
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
        //recyclerView

    }//oncreate


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_activity")) {
                finish();
                // DO WHATEVER YOU WANT.
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.addOnItemTouchListener(touchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_orders,menu);
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
                Toast.makeText(OrdersActivity.this, R.string.action_logout, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                //Intent intent1 = new Intent(OrdersActivity.this, HelpActivity.class);
                //startActivity(intent1);
                Toast.makeText(OrdersActivity.this, R.string.action_settings, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_refresh:
                key= UUID.randomUUID().toString();
                auth_key=md5(key+"TaxiIneu");
                getOrders(key, auth_key, user, md5(pass));
                //Toast.makeText(OrdersActivity.this, R.string.action_refresh, Toast.LENGTH_SHORT).show();
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
                            finish();
                            //onBackPressed();
                            //Toast.makeText(MainActivity.this,"pass_ok",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(pass_bad)){
                            Toast.makeText(OrdersActivity.this,"pass_bad",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(bad_user)){
                            Toast.makeText(OrdersActivity.this,"bad_user",Toast.LENGTH_LONG).show();
                        }else {
                            //System.out.println("buff:" + response.trim());
                            Toast.makeText(OrdersActivity.this,response.trim(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrdersActivity.this,error.toString(),Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(OrdersActivity.this);
        requestQueue.add(stringRequest);

    }

    private void getOrders(String key, String auth_key, String id, String parola){
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
                            Toast.makeText(OrdersActivity.this,"pass_bad",Toast.LENGTH_LONG).show();
                        }else if(response.trim().equals(bad_user)){
                            Toast.makeText(OrdersActivity.this,"bad_user",Toast.LENGTH_LONG).show();
                        }else {


                            Boolean flag = false;
                            try {
                                String buff="";
                                //String buff=getResources().getString(R.string.no_order)+"!";
                                //final List<Task> taskList = new ArrayList<>();
                                taskList = new ArrayList<>();


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

                                    //Task task = new Task(order_id.toString(),adresa);
                                    //task String name, String desc, Integer id, String phone, String CURRENT_TIME, String minutes
                                    Task task = new Task(nume,adresa,order_id, tel, CURRENT_TIME, minutes);
                                    taskList.add(task);

                                    //buff=String.valueOf(id);
                                    //buff=buff+id.toString()+" "+nr_masina2+"\n";
                                    //buff=buff+"id="+order_id.toString()+" "+mac+" "+adresa+"\n";
                                    flag = true;

                                }
                                recyclerviewAdapter.setTaskList(taskList);
                                recyclerView.setAdapter(recyclerviewAdapter);
                                //buff="Comenzi:\n"+buff;
                                //Toast.makeText(OrdersActivity.this, buff, Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                flag = true;
                                //Tv_status.setText("catch"+e.getMessage());
                                Toast.makeText(OrdersActivity.this, "catch" + e.getMessage(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();

                            }

                            if (!flag) {
                                //System.out.println("buff:" + response.trim());
                                Toast.makeText(OrdersActivity.this, response.trim(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrdersActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //lat, lon, nume, tel, mac, adresa, link, obs, key
                Map<String,String> params = new HashMap<String, String>();
                params.put("opt","getorders");
                params.put("key",key);
                params.put("auth_key",auth_key);
                params.put("id",id);
                params.put("parola",parola);
                params.putAll(params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OrdersActivity.this);
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
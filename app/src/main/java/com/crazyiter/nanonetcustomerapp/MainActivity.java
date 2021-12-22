package com.crazyiter.nanonetcustomerapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crazyiter.nanonetcustomerapp.db.Customer;
import com.crazyiter.nanonetcustomerapp.db.CustomerManager;
import com.crazyiter.nanonetcustomerapp.db.ProviderManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.CookieManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mainDL;
    private Customer customer;
    private TextView nameTV;
    private TextView amountTV;
    private TextView mainNameTV;
    private TextView costTV;
    private TextView daysTV;
    private TextView nanoTV;
    private TextView CCQTV;
    int level=0;
    double ccq=0;
    ProgressDialog mProgressDialog;
    // # Constants used in this example
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    final String LOGIN_FORM_URL = "https://10.136.210.61/login.cgi?uri=/";
    final String LOGIN_ACTION_URL = "https://10.136.210.61/login.cgi?uri=/";
    final String USERNAME = "ubnt";
    final String PASSWORD = "ali1985";
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainDL = findViewById(R.id.mainDL);

        NavigationView mainNV = findViewById(R.id.mainNV);
        View navHeaderView = mainNV.getHeaderView(0);
        nameTV = navHeaderView.findViewById(R.id.nameTV);

        mainNameTV = findViewById(R.id.mainNameTV);
        amountTV = findViewById(R.id.amountTV);
        costTV = findViewById(R.id.costTV);
        daysTV = findViewById(R.id.daysTV);
        nanoTV=findViewById(R.id.nanoTV);
        CCQTV=findViewById(R.id.ccqTV);
        handleSSLHandshake();

      /*  Context context=this;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

         level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels); */


        LinearLayout logoutLL = navHeaderView.findViewById(R.id.logoutLL);
        logoutLL.setOnClickListener(this);

        LinearLayout alertsLL = navHeaderView.findViewById(R.id.alertsLL);
        alertsLL.setOnClickListener(this);

        LinearLayout servicesLL = navHeaderView.findViewById(R.id.servicesLL);
        servicesLL.setOnClickListener(this);

        LinearLayout plansLL = navHeaderView.findViewById(R.id.plansLL);
        plansLL.setOnClickListener(this);

        LinearLayout logsLL = navHeaderView.findViewById(R.id.logsLL);
        logsLL.setOnClickListener(this);

        LinearLayout ticketsLL = navHeaderView.findViewById(R.id.ticketsLL);
        ticketsLL.setOnClickListener(this);

        ImageView menuIV = findViewById(R.id.menuIV);
        menuIV.setOnClickListener(this);


        customer = Customer.getShared(this);
        new Title().execute();
      //  loadnan();
        showData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomerManager.getCustomer(customer.getId(), c -> {
            if (c == null) {
                logout();
                startActivity(new Intent(this, ErrorActivity.class)
                        .putExtra("type", ErrorActivity.ERROR));
                finish();
                return;
            }

            if (!c.isActive()) {
                startActivity(new Intent(this, ErrorActivity.class)
                        .putExtra("type", ErrorActivity.NOT_ACTIVE));
                finish();
                return;
            }

            if (!c.getEmail().equals(customer.getEmail()) || !c.getPassword().equals(customer.getPassword())) {
                logout();
                startActivity(new Intent(this, ErrorActivity.class)
                        .putExtra("type", ErrorActivity.CHANGED));
                finish();
                return;
            }

            ProviderManager.getProvider(c.getProviderId(), p -> {
                if (!p.isActive()) {
                    startActivity(new Intent(this, ErrorActivity.class)
                            .putExtra("type", ErrorActivity.NOT_ACTIVE));
                    finish();
                    return;
                }

                customer = c;
                showData();
                internetSpeed();
            });
        });
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void internetSpeed() {
        TextView netTV = findViewById(R.id.netTV);
      //  CheckInternet.check((double bitsPerSec, String type) -> {
        //    netTV.setText("" + bitsPerSec + " " + type);
      //  });
    }

    @SuppressLint("SetTextI18n")
    private void showData() {
        nameTV.setText(customer.getName());
        mainNameTV.setText(customer.getName());
        amountTV.setText(Statics.formatNumber(customer.getAmount() * 1000));
        costTV.setText(Statics.formatNumber(customer.getCost() * 1000));
        int dif = customer.getDiffDaysColor();
        daysTV.setTextColor(getResources().getColor(dif));
        if (dif == R.color.colorRed) {
            daysTV.setText(getString(R.string.expired));
        } else {
            daysTV.setText(customer.getDiffDays() + " يوم و" + customer.getRemainsHours() + " ساعة");
        }
        CCQTV.setText(String.valueOf(ccq));
        if (level >= -40) {
            nanoTV.setText("الأشارة ممتازة");
        } else if (level >= -50) {
            nanoTV.setText("الأشارة جيدة جداً");
        } else if (level >= -60) {
             nanoTV.setText("الأشارة جيدة");
        } else if (level >= -70) {
            nanoTV.setText("الأشارة ليست جيدة");

        } else {
            //Too low signal
        }
    }

    @Override
    public void onBackPressed() {
        if (mainDL.isDrawerOpen(GravityCompat.START)) {
            mainDL.closeDrawer(GravityCompat.START);
            return;
        }

        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuIV:
                mainDL.openDrawer(GravityCompat.START);
                break;

            case R.id.servicesLL:
                startActivity(new Intent(this, ServicesActivity.class));
                break;

            case R.id.plansLL:
                startActivity(new Intent(this, PlansActivity.class));
                break;

            case R.id.logsLL:
                startActivity(new Intent(this, CustomerLogActivity.class));
                break;

            case R.id.ticketsLL:
                startActivity(new Intent(this, TicketsActivity.class));
                break;

            case R.id.alertsLL:
                startActivity(new Intent(this, AlertsActivity.class));
                break;

            case R.id.logoutLL:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.sure_logout))
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                            logout();
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        }).create().show();
                break;
        }
    }

    private void logout() {
        CustomerManager.logout(customer);
        FirebaseFirestore.getInstance().clearPersistence();
        Customer.logout(this);
    }
    private void loadnan() {
        //getting the progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://"+customer.getNanoUrl()+"/status.cgi"+"?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion

                        progressBar.setVisibility(View.INVISIBLE);

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named tutorial inside the object
                            //so here we are getting that json array
                            JSONObject wireless = obj.getJSONObject("wireless");
                          level=  wireless.getInt("signal");
                          ccq=wireless.getInt("ccq")/10;

                            //now looping through all the elements of the json array




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occur
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
    private class Title extends AsyncTask<Void, Void, Void>
    {
        String title;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Portal CEFSA");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Connection.Response loginForm = null;
            try {
                loginForm = Jsoup.connect(LOGIN_FORM_URL)
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .timeout(20000)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Document loginDoc = null; // this is the document containing response html
            try {
                loginDoc = loginForm.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HashMap<String, String> cookies = new HashMap<>(loginForm.cookies()); // save the cookies to be passed on to next request

// # Prepare login credentials
          /*  String authToken = loginDoc.select("#login > form > div:nth-child(1) > input[type=\"hidden\"]:nth-child(2)")
                    .first()
                    .attr("value");*/

            HashMap<String, String> formData = new HashMap<>();


            formData.put("username", USERNAME);
            formData.put("password", PASSWORD);

           // formData.put("authenticity_token", authToken);

// # Now send the form for login
            Connection.Response homePage = null;
            try {
                homePage = Jsoup.connect(LOGIN_ACTION_URL)
                        .cookies(cookies)
                        .data(formData)
                        .timeout(20000)
                        .method(Connection.Method.POST)
                        .userAgent(USER_AGENT)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                System.out.println(homePage.parse().html());
                String tmpHtml = homePage.parse().html();
                Element element = homePage.selectFirst("pre#id");
                String jsonText = element .data();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {



            // Set title into TextView
          //  TextView txt_teste = (TextView) findViewById(R.id.txt_teste);
          //  txt_teste.setText(title);
            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        }
    }
}

package com.crazyiter.nanonetcustomerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetcustomerapp.db.AlertManager;
import com.crazyiter.nanonetcustomerapp.db.Customer;

public class AlertsActivity extends AppCompatActivity {

    private RecyclerView alertsRV;
    private TextView noTV;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        customer = Customer.getShared(this);

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());

        noTV = findViewById(R.id.noTV);
        alertsRV = findViewById(R.id.alertsRV);
        alertsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setupData();
    }

    private void setupData() {
        AlertManager.getAlerts(customer, alerts -> {
            if (alerts == null || alerts.isEmpty()) {
                noTV.setVisibility(View.VISIBLE);
                alertsRV.setVisibility(View.GONE);
            } else {
                noTV.setVisibility(View.GONE);
                alertsRV.setVisibility(View.VISIBLE);
                alertsRV.setAdapter(new AlertsAdapter(this, alerts));
            }
        });
    }
}
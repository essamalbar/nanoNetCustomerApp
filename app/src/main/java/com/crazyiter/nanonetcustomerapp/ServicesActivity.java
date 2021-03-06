package com.crazyiter.nanonetcustomerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetcustomerapp.db.Customer;
import com.crazyiter.nanonetcustomerapp.db.ServiceManager;

public class ServicesActivity extends AppCompatActivity {

    private Customer customer;
    private RecyclerView servicesRV;
    private TextView noTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        customer = Customer.getShared(this);

        noTV = findViewById(R.id.noTV);
        servicesRV = findViewById(R.id.servicesRV);
        servicesRV.setLayoutManager(new GridLayoutManager(this, 2));

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceManager.getServices(customer.getProviderId(), serviceModels -> {
            if (serviceModels == null || serviceModels.isEmpty()) {
                noTV.setVisibility(View.VISIBLE);
                servicesRV.setVisibility(View.GONE);
            } else {
                noTV.setVisibility(View.GONE);
                servicesRV.setVisibility(View.VISIBLE);

                servicesRV.setAdapter(new ServiceAdapter(this, serviceModels));
            }
        });
    }
}
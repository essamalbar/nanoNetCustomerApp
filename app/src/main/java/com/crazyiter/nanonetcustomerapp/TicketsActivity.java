package com.crazyiter.nanonetcustomerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;
import com.crazyiter.nanonetcustomerapp.db.Customer;
import com.crazyiter.nanonetcustomerapp.db.TicketModel;
import com.crazyiter.nanonetcustomerapp.db.TicketsManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class TicketsActivity extends AppCompatActivity {

    private TextView noTV;
    private CardView rootCV;
    private RecyclerView ticketsRV;
    private Customer customer;
    private ArrayList<TicketModel> ticketModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        customer = Customer.getShared(this);
        VolleyManager.REQUEST_QUEUE = Volley.newRequestQueue(getApplicationContext());

        noTV = findViewById(R.id.noTV);
        rootCV = findViewById(R.id.rootCV);

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());

        ticketsRV = findViewById(R.id.ticketsRV);
        ticketsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ExtendedFloatingActionButton sendFAB = findViewById(R.id.newTicketEFAB);
        sendFAB.setOnClickListener(v -> {
            String currentDateTime = Statics.LocalDate.getCurrentDate();
            if (ticketModels != null && !ticketModels.isEmpty()) {
                int difHours = Statics.LocalDate.getAllDifferenceHours(ticketModels.get(0).getDateTime(), currentDateTime);
                if (difHours < 24) {
                    Statics.showSnackBar(rootCV, "يجب مرور 24 ساعة على التيكت السابقة");
                    return;
                }
            }

            new NewTicketDialog(this, customer, ticket -> {
                new TicketAnswersBottomDialog(ticket).show(getSupportFragmentManager());
            }).show();
        });

        ticketsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && sendFAB.getVisibility() == View.VISIBLE) {
                    sendFAB.hide();
                } else if (dy < 0 && sendFAB.getVisibility() != View.VISIBLE) {
                    sendFAB.show();
                }
            }
        });

        setupDate();
        openTicket();

    }

    private void setupDate() {
        TicketsManager.getMyTickets(customer, tickets -> {
            this.ticketModels = tickets;
            if (tickets == null || tickets.isEmpty()) {
                noTV.setVisibility(View.VISIBLE);
                ticketsRV.setVisibility(View.GONE);
            } else {
                noTV.setVisibility(View.GONE);
                ticketsRV.setVisibility(View.VISIBLE);
                ticketsRV.setAdapter(new TicketAdapter(this, tickets));
            }
        });
    }

    private void openTicket() {
        try {
            String ticketId = getIntent().getStringExtra("ticketId");
            TicketsManager.findTicket(ticketId, t -> {
                if (t != null)
                    new TicketAnswersBottomDialog(t).show(getSupportFragmentManager());
            });
        } catch (Exception ignored) {
        }
    }

}
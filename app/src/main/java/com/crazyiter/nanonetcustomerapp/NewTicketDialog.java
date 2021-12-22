package com.crazyiter.nanonetcustomerapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.crazyiter.nanonetcustomerapp.db.Customer;
import com.crazyiter.nanonetcustomerapp.db.TicketModel;
import com.crazyiter.nanonetcustomerapp.db.TicketsManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.UUID;

public class NewTicketDialog extends Dialog {

    private final Customer customer;
    private final OnAddTicket onAddTicket;
    private TextInputLayout ticketTIL;

    public NewTicketDialog(@NonNull Context context, Customer customer, OnAddTicket onAddTicket) {
        super(context);
        this.customer = customer;
        this.onAddTicket = onAddTicket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_ticket);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ticketTIL = findViewById(R.id.ticketTIL);
        Button sendBTN = findViewById(R.id.sendBTN);

        sendBTN.setOnClickListener(v -> {
            String body = ticketTIL.getEditText().getText().toString().trim();
            if (body.isEmpty()) {
                ticketTIL.setError(getContext().getString(R.string.enter_ticket));
            } else {
                TicketModel ticketModel = new TicketModel(UUID.randomUUID().toString(), customer.getId(), customer.getProviderId(), body, Statics.LocalDate.getCurrentDate(), true);
                TicketsManager.addTicket(ticketModel);
                onAddTicket.onAdded(ticketModel);
                dismiss();
            }
        });

    }

    public interface OnAddTicket {
        void onAdded(TicketModel ticketModel);
    }

}

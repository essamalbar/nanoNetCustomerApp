package com.crazyiter.nanonetcustomerapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetcustomerapp.db.Customer;
import com.crazyiter.nanonetcustomerapp.db.TicketModel;
import com.crazyiter.nanonetcustomerapp.db.TicketsManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.UUID;

public class TicketAnswersBottomDialog extends BottomSheetDialogFragment implements TextWatcher {

    private RecyclerView answersRV;
    private TextView noTV;
    private TicketModel ticketModel;
    private ImageView sendIV;
    private EditText replyET;
    private LinearLayout replyLL;
    private TextView lockTV;
    private Customer customer;

    public TicketAnswersBottomDialog(TicketModel ticketModel) {
        this.ticketModel = ticketModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_dialog_ticket_answers, container, false);

        customer = Customer.getShared(requireContext());

        noTV = view.findViewById(R.id.noTV);
        replyLL = view.findViewById(R.id.replyLL);
        lockTV = view.findViewById(R.id.lockTV);
        sendIV = view.findViewById(R.id.sendIV);

        replyET = view.findViewById(R.id.replyET);
        replyET.addTextChangedListener(this);

        answersRV = view.findViewById(R.id.answersRV);
        answersRV.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        TextView dateTimeTV = view.findViewById(R.id.dateTimeIV);
        TextView bodyTV = view.findViewById(R.id.bodyTV);

        dateTimeTV.setText(ticketModel.getDateTime());
        bodyTV.setText(ticketModel.getBody());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TicketsManager.getTicket(ticketModel.getId(), ticket -> {
            this.ticketModel = ticket;
            if (ticketModel == null || !ticketModel.isActive()) {
                lockTV.setVisibility(View.VISIBLE);
                replyLL.setVisibility(View.GONE);
            } else {
                lockTV.setVisibility(View.GONE);
                replyLL.setVisibility(View.VISIBLE);
            }

            if (ticketModel.getAnswerModels().isEmpty()) {
                noTV.setVisibility(View.VISIBLE);
                answersRV.setVisibility(View.GONE);
            } else {
                noTV.setVisibility(View.GONE);
                answersRV.setVisibility(View.VISIBLE);
                answersRV.setAdapter(
                        new AnswerAdapter(
                                getContext(),
                                customer.getId(),
                                ticket
                        )
                );
                answersRV.scrollToPosition(ticket.getAnswerModels().size() - 1);
            }
        });
    }

    public void show(@NonNull FragmentManager manager) {
        super.show(manager, getTag());
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().trim().isEmpty()) {
            sendIV.setColorFilter(getResources().getColor(R.color.gray));
            sendIV.setOnClickListener(null);
        } else {
            sendIV.setColorFilter(getResources().getColor(R.color.colorPrimary));
            sendIV.setOnClickListener(v -> {
                TicketModel.TicketAnswerModel model = new TicketModel.TicketAnswerModel(
                        UUID.randomUUID().toString(),
                        ticketModel.getCustomerId(),
                        s.toString().trim(),
                        Statics.LocalDate.getCurrentDate()
                );
                TicketsManager.addAnswer(ticketModel, model);
                replyET.setText("");
            });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
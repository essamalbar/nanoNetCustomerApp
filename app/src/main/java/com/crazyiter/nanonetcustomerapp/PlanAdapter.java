package com.crazyiter.nanonetcustomerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetcustomerapp.db.PlanModel;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<PlanModel> planModels;

    public PlanAdapter(Context context, ArrayList<PlanModel> planModels) {
        this.context = context;
        this.planModels = planModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_plans, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlanModel model = planModels.get(position);
        holder.setupData(model);
    }

    @Override
    public int getItemCount() {
        return planModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView planTV;
        private final TextView planPriceTV;
        private final TextView planDescriptionTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            planTV = itemView.findViewById(R.id.planTV);
            planPriceTV = itemView.findViewById(R.id.planPriceTV);
            planDescriptionTV = itemView.findViewById(R.id.planDescriptionTV);
        }

        @SuppressLint("SetTextI18n")
        private void setupData(PlanModel planModel) {
            planTV.setText(planModel.getName());
            planDescriptionTV.setText(planModel.getDescription());
            planPriceTV.setText(Statics.formatNumber(planModel.getPrice() * 1000) + " دينار");
        }

    }
}

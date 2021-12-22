package com.crazyiter.nanonetcustomerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetcustomerapp.db.ServiceModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<ServiceModel> serviceModels;

    public ServiceAdapter(Context context, ArrayList<ServiceModel> serviceModels) {
        this.context = context;
        this.serviceModels = serviceModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_service, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceModel model = serviceModels.get(position);
        holder.setupData(model);

        holder.itemView.setOnClickListener(v -> Statics.openBrowser(context, model.getUrl()));

    }

    @Override
    public int getItemCount() {
        return serviceModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView serviceIV;
        private final TextView serviceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceIV = itemView.findViewById(R.id.serviceIV);
            serviceTV = itemView.findViewById(R.id.serviceTV);
        }

        private void setupData(ServiceModel serviceModel) {
            serviceTV.setText(serviceModel.getName());
            Picasso.get()
                    .load(serviceModel.getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(serviceIV);
        }

    }
}

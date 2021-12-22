package com.crazyiter.nanonetcustomerapp.db;

import com.crazyiter.nanonetcustomerapp.Statics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class ServiceManager {

    private static final String col = "services";

    public static void getServices(String providerId, LoadServicesListener loadServicesListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .whereEqualTo("providerId", providerId)
                .addSnapshotListener((values, error) -> {
                    if (values == null) {
                        loadServicesListener.onLoad(null);
                    } else {
                        ArrayList<ServiceModel> serviceModels = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : values) {
                            ServiceModel serviceModel = new ServiceModel(
                                    doc.getId(),
                                    doc.getString("name"),
                                    doc.getString("url"),
                                    doc.getString("image"),
                                    doc.getString("providerId"),
                                    doc.getString("date")
                            );
                            serviceModels.add(serviceModel);
                        }

                        Collections.sort(serviceModels, (o1, o2) -> {
                            long t1 = Statics.LocalDate.getTime(o1.getDate());
                            long t2 = Statics.LocalDate.getTime(o2.getDate());
                            if (t1 > t2) return -1;
                            else if (t1 < t2) return 1;
                            return 0;
                        });
                        loadServicesListener.onLoad(serviceModels);
                    }
                });

    }

    public interface LoadServicesListener {
        void onLoad(ArrayList<ServiceModel> serviceModels);
    }

}

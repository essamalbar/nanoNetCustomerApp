package com.crazyiter.nanonetcustomerapp.db;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PlanManager {

    private static final String col = "plans";

    public static void getPlans(String providerId, LoadServicesListener loadServicesListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .whereEqualTo("providerId", providerId)
                .addSnapshotListener((values, error) -> {
                    if (values == null) {
                        loadServicesListener.onLoad(null);
                    } else {
                        ArrayList<PlanModel> planModels = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : values) {
                            PlanModel serviceModel = new PlanModel(
                                    doc.getId(),
                                    doc.getString("name"),
                                    Integer.parseInt(String.valueOf(doc.get("price"))),
                                    doc.getString("description"),
                                    doc.getString("providerId")
                            );
                            planModels.add(serviceModel);
                        }

                        loadServicesListener.onLoad(planModels);
                    }
                });

    }

    public interface LoadServicesListener {
        void onLoad(ArrayList<PlanModel> planModels);
    }

}

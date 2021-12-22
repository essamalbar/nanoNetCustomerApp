package com.crazyiter.nanonetcustomerapp.db;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

public class ProviderManager {

    private final static String col = "providers";

    public static void getProvider(String id, LoadProviderListener loadProviderListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(id)
                .get()
                .addOnSuccessListener(doc -> {
                    try {
                        Provider provider = new Provider(
                                doc.getId(),
                                doc.getString("name"),
                                doc.getString("email"),
                                doc.getString("startDate"),
                                Integer.parseInt(String.valueOf(doc.get("type"))),
                                Integer.parseInt(String.valueOf(doc.get("count"))),
                                0,
                                doc.getString("adminId")
                        );

                        provider.setFcm(doc.getString("fcm"));
                        provider.setActive(doc.getBoolean("isActive"));
                        provider.setRenewAlert(doc.getBoolean("renewAlert"));
                        provider.setPayAlert(doc.getBoolean("payAlert"));
                        provider.setAmountAlert(doc.getBoolean("amountAlert"));
                        provider.setTicketAlert(doc.getBoolean("ticketAlert"));

                        loadProviderListener.onLogin(provider);
                    } catch (Exception e) {
                        Log.e("provider", "" + e.getMessage());
//                        loadProviderListener.onLogin(null);
                    }
                });
    }

    public interface LoadProviderListener {
        void onLogin(Provider provider);
    }

}

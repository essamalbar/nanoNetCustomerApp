package com.crazyiter.nanonetcustomerapp.db;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class CustomerManager {

    private final static String col = "customers";

    public static void login(String email, String password, GetSingleCustomerListener getSingleCustomerListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        boolean u = email.equals(doc.getString("email"));
                        boolean p = password.equals(doc.getString("password"));
                        if (u && p) {
                            getCustomer(doc.getId(), getSingleCustomerListener);
                            return;
                        }
                    }
                    getSingleCustomerListener.onLoad(null);
                });
    }

    public static void getCustomer(String customerId, GetSingleCustomerListener getSingleCustomerListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(customerId)
                .get()
                .addOnSuccessListener(value -> {
                    if (value == null) {
                        getSingleCustomerListener.onLoad(null);
                    } else {

                        boolean isActive;
                        try {
                            isActive = value.getBoolean("isActive");
                        } catch (Exception e) {
                            isActive = true;
                        }

                        try {
                            Customer c = new Customer(
                                    value.getId(),
                                    value.getString("name"),
                                    value.getString("user"),
                                    value.getString("mobile"),
                                    value.getString("startDate"),
                                    Integer.parseInt(String.valueOf(value.get("cost"))),
                                    value.getString("providerId"),
                                    Integer.parseInt(String.valueOf(value.get("amount"))),
                                    value.getString("nanoUrl"),
                                    value.getString("nanoUsername"),
                                    value.getString("nanoPassword"),
                                    value.getString("note"),
                                    value.getString("email"),
                                    value.getString("password"),
                                    Integer.parseInt(String.valueOf(value.get("days")))
                            );
                            c.setActive(isActive);
                            c.setLogs(new JSONArray(value.getString("logs")));
                            getSingleCustomerListener.onLoad(c);
                        } catch (Exception e) {
                            getSingleCustomerListener.onLoad(null);
                        }

                    }
                });
    }

    public static void updateCustomer(Customer customer) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(customer.getId())
                .update(customer.getMap());
    }

    public static void logout(Customer customer) {
        Map<String, Object> map = new HashMap<>();
        map.put("fcm", "");
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(customer.getId())
                .update(map);
    }

    public interface GetSingleCustomerListener {
        void onLoad(Customer customers);
    }

}

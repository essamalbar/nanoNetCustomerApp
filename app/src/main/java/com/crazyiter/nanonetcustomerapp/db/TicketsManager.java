package com.crazyiter.nanonetcustomerapp.db;

import com.crazyiter.nanonetcustomerapp.Statics;
import com.crazyiter.nanonetcustomerapp.VolleyManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class TicketsManager {

    private static final String col = "tickets";

    public static void findTicket(String ticketId, LoadTicketListener loadTicketListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(ticketId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc == null) {
                        loadTicketListener.onLoad(null);
                    } else {
                        TicketModel ticketModel;
                        boolean active;
                        try {
                            active = doc.getBoolean("isActive");
                        } catch (Exception e) {
                            active = false;
                        }
                        ticketModel = new TicketModel(
                                doc.getId(),
                                doc.getString("customerId"),
                                doc.getString("providerId"),
                                doc.getString("body"),
                                doc.getString("dateTime"),
                                active
                        );
                        String answers = doc.getString("answers");
                        try {
                            if (answers == null || answers.isEmpty()) {
                                ticketModel.addAnswers(new JSONArray());
                            } else {
                                ticketModel.addAnswers(new JSONArray(answers));
                            }
                        } catch (JSONException e) {
                            ticketModel.addAnswers(new JSONArray());
                        }
                        loadTicketListener.onLoad(ticketModel);

                    }
                });

    }

    public static void getTicket(String ticketId, LoadTicketListener loadTicketListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(ticketId)
                .addSnapshotListener((doc, error) -> {
                    if (doc == null) {
                        loadTicketListener.onLoad(null);
                    } else {
                        boolean active;
                        try {
                            active = doc.getBoolean("isActive");
                        } catch (Exception e) {
                            active = false;
                        }
                        TicketModel ticketModel = new TicketModel(
                                doc.getId(),
                                doc.getString("customerId"),
                                doc.getString("providerId"),
                                doc.getString("body"),
                                doc.getString("dateTime"),
                                active
                        );
                        String answers = doc.getString("answers");
                        try {
                            if (answers == null || answers.isEmpty()) {
                                ticketModel.addAnswers(new JSONArray());
                            } else {
                                ticketModel.addAnswers(new JSONArray(answers));
                            }
                        } catch (JSONException e) {
                            ticketModel.addAnswers(new JSONArray());
                        }
                        loadTicketListener.onLoad(ticketModel);
                    }
                });

    }

    public static void getMyTickets(Customer customer, LoadTicketsListener loadTicketsListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .whereEqualTo("customerId", customer.getId())
                .addSnapshotListener((values, error) -> {
                    if (values == null) {
                        loadTicketsListener.onLoad(null);
                    } else {
                        ArrayList<TicketModel> ticketModels = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : values) {
                            TicketModel ticketModel = new TicketModel(
                                    doc.getId(),
                                    doc.getString("customerId"),
                                    doc.getString("providerId"),
                                    doc.getString("body"),
                                    doc.getString("dateTime"),
                                    doc.getBoolean("isActive")
                            );

                            try {
                                ticketModel.addAnswers(new JSONArray(doc.getString("answers")));
                            } catch (JSONException ignored) {
                            }

                            ticketModels.add(ticketModel);
                        }

                        Collections.sort(ticketModels, (o1, o2) -> {
                            long t1 = Statics.LocalDate.getTime(o1.getDateTime());
                            long t2 = Statics.LocalDate.getTime(o2.getDateTime());
                            if (t1 > t2) return -1;
                            else if (t1 < t2) return 1;
                            return 0;
                        });
                        loadTicketsListener.onLoad(ticketModels);
                    }
                });

    }

    public static void addTicket(TicketModel ticketModel) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(ticketModel.getId())
                .set(ticketModel.getMap());

        ProviderManager.getProvider(ticketModel.getProviderId(), t -> {
            if (t.isTicketAlert())
                VolleyManager.sendNotification(t.getFcm(), "تكيت جديدة", ticketModel.getBody(), ticketModel.getId());
        });
    }

    public static void addAnswer(TicketModel ticketModel, TicketModel.TicketAnswerModel answerModel) {
        ticketModel.addAnswer(answerModel);
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(ticketModel.getId())
                .update(ticketModel.getMap());

        ProviderManager.getProvider(ticketModel.getProviderId(), t -> VolleyManager.sendNotification(t.getFcm(), "رد جديد", answerModel.getBody(), ticketModel.getId()));
    }

    public static void deleteAnswer(TicketModel ticketModel, TicketModel.TicketAnswerModel answerModel) {
        ticketModel.deleteAnswer(answerModel);
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(ticketModel.getId())
                .update(ticketModel.getMap());
    }

    public static void deleteTicket(TicketModel ticketModel) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(ticketModel.getId())
                .delete();
    }

    public interface LoadTicketsListener {
        void onLoad(ArrayList<TicketModel> ticketModels);
    }

    public interface LoadTicketListener {
        void onLoad(TicketModel ticketModel);
    }

}

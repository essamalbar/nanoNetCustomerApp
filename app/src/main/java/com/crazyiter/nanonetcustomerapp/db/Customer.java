package com.crazyiter.nanonetcustomerapp.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.crazyiter.nanonetcustomerapp.FirebaseCloudMessagingService;
import com.crazyiter.nanonetcustomerapp.R;
import com.crazyiter.nanonetcustomerapp.Statics;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Customer implements Serializable {
    private final String id;
    private final String name;
    private final String user;
    private final String mobile;
    private final String startDate;
    private final int cost;
    private final int days;
    private final String providerId;
    private final int amount;

    private final String nanoUrl;
    private final String nanoUsername;
    private final String nanoPassword;

    private final String note;

    private final String email;
    private final String password;
    private JSONArray logs;
    private String fcm;

    private boolean isActive;

    public void saveShared(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("user", user);
        editor.putString("mobile", mobile);
        editor.putString("startDate", startDate);
        editor.putInt("cost", cost);
        editor.putInt("days", days);
        editor.putString("providerId", providerId);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putInt("amount", amount);
        editor.putString("nanoUrl", nanoUrl);
        editor.putString("nanoUsername", nanoUsername);
        editor.putString("nanoPassword", nanoPassword);
        editor.apply();
    }

    public static Customer getShared(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String _id = sharedPreferences.getString("id", null);
        if (_id == null) {
            return null;
        }

        Customer customer = new Customer(
                _id,
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("user", null),
                sharedPreferences.getString("mobile", null),
                sharedPreferences.getString("startDate", null),
                sharedPreferences.getInt("cost", -1),
                sharedPreferences.getString("providerId", null),
                sharedPreferences.getInt("amount", -1),
                sharedPreferences.getString("nanoUrl", null),
                sharedPreferences.getString("nanoUsername", null),
                sharedPreferences.getString("nanoPassword", null),
                sharedPreferences.getString("note", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getInt("days", 0)

        );
        customer.setFcm(FirebaseCloudMessagingService.getToken(context));
        return customer;
    }

    public static void logout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public Customer(String id, String name, String user, String mobile, String startDate, int cost, String providerId, int amount, String nanoUrl, String nanoUsername, String nanoPassword, String note, String email, String password, int days) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.mobile = mobile;
        this.startDate = startDate;
        this.cost = cost;
        this.providerId = providerId;
        this.amount = amount;
        this.nanoUrl = nanoUrl;
        this.nanoUsername = nanoUsername;
        this.nanoPassword = nanoPassword;
        this.note = note;
        this.email = email;
        this.password = password;
        this.days = days;
    }

    public JSONArray getLogs() {
        if (logs == null) {
            logs = new JSONArray();
        }
        return logs;
    }

    public void setLogs(JSONArray logs) {
        this.logs = logs;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("user", user);
        map.put("email", email);
        map.put("password", password);
        map.put("mobile", mobile);
        map.put("startDate", startDate);
        map.put("days", days);
        map.put("cost", cost);
        map.put("providerId", providerId);
        map.put("amount", amount);

        map.put("nanoUrl", nanoUrl);
        map.put("nanoUsername", nanoUsername);
        map.put("nanoPassword", nanoPassword);

        map.put("note", note);
        map.put("fcm", fcm);
        map.put("logs", getLogs().toString());

        return map;
    }

    public boolean isLinked() {
        return !user.isEmpty() && !password.isEmpty();
    }

    public String getEndDate() {
        return Statics.LocalDate.getEndDateAddDays(startDate, days);
    }

    public int getDiffDays() {
        String d = Statics.LocalDate.getCurrentDate();
        String d2 = getEndDate();

        return Statics.LocalDate.getDifferenceDays(d, d2);
    }

    public int getRemainsHours() {
        String d = Statics.LocalDate.getCurrentDate();
        String d2 = getEndDate();

        return Statics.LocalDate.getDifferenceHours(d, d2);
    }

    public int getDiffDaysColor() {
        int dif = getDiffDays();
        int difHours = getRemainsHours();

        if (dif > 3) {
            return R.color.colorGreen;
        }

        if (dif > 0 || (dif == 0 && difHours > 0)) {
            return R.color.colorYellow;
        }

        return R.color.colorRed;
    }

    public int getDays() {
        return days;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getMobile() {
        return mobile;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getEmail() {
        return email;
    }

    public int getAmount() {
        return amount;
    }

    public int getCost() {
        return cost;
    }

    public String getNanoUrl() {
        return nanoUrl;
    }

    public String getNanoUsername() {
        return nanoUsername;
    }

    public String getNanoPassword() {
        return nanoPassword;
    }

    public String getNote() {
        return note;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
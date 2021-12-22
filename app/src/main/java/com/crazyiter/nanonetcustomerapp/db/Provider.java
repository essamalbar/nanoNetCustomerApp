package com.crazyiter.nanonetcustomerapp.db;

import java.io.Serializable;

public class Provider implements Serializable {
    private String id;
    private String name;
    private String email;
    private String startDate;
    private int type;
    private int count;
    private int orderBy;
    private String fcm;
    private String adminId;
    private boolean isOnline;

    private boolean isActive = true;
    private boolean renewAlert = false;
    private boolean payAlert = false;
    private boolean amountAlert = false;
    private boolean ticketAlert = false;

    public Provider(String id, String name, String email, String startDate, int type, int count, int orderBy, String adminId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.startDate = startDate;
        this.type = type;
        this.count = count;
        this.orderBy = orderBy;
        this.adminId = adminId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isRenewAlert() {
        return renewAlert;
    }

    public void setRenewAlert(boolean renewAlert) {
        this.renewAlert = renewAlert;
    }

    public boolean isPayAlert() {
        return payAlert;
    }

    public void setPayAlert(boolean payAlert) {
        this.payAlert = payAlert;
    }

    public boolean isAmountAlert() {
        return amountAlert;
    }

    public void setAmountAlert(boolean amountAlert) {
        this.amountAlert = amountAlert;
    }

    public boolean isTicketAlert() {
        return ticketAlert;
    }

    public void setTicketAlert(boolean ticketAlert) {
        this.ticketAlert = ticketAlert;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
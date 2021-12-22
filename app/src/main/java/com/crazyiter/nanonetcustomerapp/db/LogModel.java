package com.crazyiter.nanonetcustomerapp.db;

public class LogModel {

    private final String id;
    private final String dateTime;
    private final String body;

    public LogModel(String id, String dateTime, String body) {
        this.id = id;
        this.dateTime = dateTime;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getBody() {
        return body;
    }
}

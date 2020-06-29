package io.wentz.models;

public class User {
    private final String uuid;

    public User(String id) {
        this.uuid = id;
    }

    public String getReportPath() {
        return "target/" + uuid + "-report.txt";
    }

    public String getUUID() {
        return uuid;
    }
}

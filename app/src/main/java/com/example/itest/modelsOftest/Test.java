package com.example.itest.modelsOftest;

public class Test {
    private String subject;
    private String theme;
    private String UserKey;
    private String key;
    private boolean completed;

    public Test() {
    }

    public String getSubject() {

        return subject;
    }

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        UserKey = userKey;
    }

    public void setSubject(String subject) {

        this.subject = subject;
    }

    public String getTheme() {

        return theme;
    }

    public void setTheme(String theme) {

        this.theme = theme;
    }


    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

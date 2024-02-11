package com.example.itest.modelsOftest;

public class Quetion {
    private String quetion;
    private String key;
    private String test_key;
    //constructors
    public Quetion(){

    }
    public Quetion(String quetion){
        this.quetion = quetion;
    }
    public String getTest_key() {
        return test_key;
    }

    public void setTest_key(String test_key) {
        this.test_key = test_key;
    }


    //getters
    public String getQuetion() {
        return quetion;
    }

    public String getKey() {
        return key;
    }


    //setters

    public void setQuetion(String quetion) {
        this.quetion = quetion;
    }

    public void setKey(String key) {
        this.key = key;
    }


}

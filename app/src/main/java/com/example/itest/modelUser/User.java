package com.example.itest.modelUser;

public class User {
    private String nickName;
    private String email;
    private String key;
    public User(){

    }
    public User(String nickName,String email){
        this.nickName = nickName;
        this.email = email;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

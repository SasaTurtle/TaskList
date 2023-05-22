package com.jecna.task.service;
import java.security.Identity;

public class SingetonToken {
    String token;
    private static final SingetonToken ourInstance = new SingetonToken();
    public static SingetonToken getInstance() {
        return ourInstance;
    }
    private SingetonToken() { }
    public void setToken(String editValue) {
        this.token = editValue;
    }
    public String getToken() {
        return token;
    }
}

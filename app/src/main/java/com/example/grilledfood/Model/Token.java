package com.example.grilledfood.Model;

public class Token {
    private String token;
    private boolean serverToken;

    public Token() {
    }

    public Token(String token, boolean serverToken) {
        this.token = token;
        this.serverToken = serverToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServerToken() {
        return serverToken;
    }

    public void setServerToken(boolean serverToken) {
        this.serverToken = serverToken;
    }
}

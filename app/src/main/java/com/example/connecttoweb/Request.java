package com.example.connecttoweb;

public class Request {

    private String kay ;
    private String val ;

    public Request(String kay, String val) {
        this.kay = kay;
        this.val = val;
    }

    public String getKay() {
        return kay;
    }

    public String getVal() {
        return val;
    }
}

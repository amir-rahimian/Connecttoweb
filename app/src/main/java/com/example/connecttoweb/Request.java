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

    @Override
    public String toString (){
     return this.kay+"="+this.val;
    }


    public static String getFullUrlFrom (String url ,Request... requests){

        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append("?");
        for (Request r :
                requests) {
            stringBuilder.append(r.toString());
            stringBuilder.append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.toString().length()-1);


        return stringBuilder.toString();
    }
}

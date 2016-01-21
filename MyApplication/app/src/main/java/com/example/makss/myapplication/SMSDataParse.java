package com.example.makss.myapplication;

import java.util.Date;

/**
 * Created by makss on 22.01.2016.
 */
public class SMSDataParse {

    /*private String[] columns;

    public String[] getColumns() {
        return  new String[] {"operation", "user", "date", "price", "location", "place", "balance"};
    }*/

    enum operation {
        Buy, Input, CashOut, Code, Cashback, Percent, Info, Deny
    }
    enum user {
        Maks, Alina
    }
    Date date;
    float price;
    String location;
    String place;
    float balance;


  /*  public SMSDataParse(String cost) {
        this.cost = cost;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }*/
}

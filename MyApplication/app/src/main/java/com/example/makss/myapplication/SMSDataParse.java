package com.example.makss.myapplication;

import java.util.Date;

/**
 * Created by makss on 22.01.2016.
 */
public class SMSDataParse {
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public enProcedure getProcedure() {
        return procedure;
    }

    public void setProcedure(enProcedure procedure) {
        this.procedure = procedure;
    }


    /*private String[] columns;

    public String[] getColumns() {
        return  new String[] {"operation", "user", "date", "price", "location", "place", "balance"};
    }*/

    enum user {
        Maks, Alina
    }
    enum currency {
        RU, USD
    }
    private Date date;
    private float price;
    private String location;
    private String place;
    private float balance;
    private enProcedure procedure;


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

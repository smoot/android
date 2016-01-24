package com.example.makss.myapplication;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by makss on 22.01.2016.
 */
public class SMSDataParse {

    private Calendar date;
    private Double coast;
    private String location;
    private String place;
    private Double balance;
    private enProcedure procedure;
    private enUser user;
    private enCurrency currency;

    //Date in local TZ
    public Calendar getDate() {
        if (date != null)
            return date;
        else return Calendar.getInstance();
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getLocation() {
        if (location != null)
         return location;
        else return "Unknown";
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public enProcedure getProcedure() {
        return procedure;
    }

    public void setProcedure(enProcedure procedure) {
        this.procedure = procedure;
    }

    public Double getCoast() {
        if (coast != null)
            return coast;
        else return 0.0;
    }

    public void setCoast(Double coast) {
        this.coast = coast;
    }

    public enUser getUser() {
        if (user != null)
            return user;
        else return enUser.Unknown;
    }

    public void setUser(enUser user) {
        this.user = user;
    }

    public enCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(enCurrency currency) {
        this.currency = currency;
    }

}

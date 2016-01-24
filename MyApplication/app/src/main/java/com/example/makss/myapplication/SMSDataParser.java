package com.example.makss.myapplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by makss on 22.01.2016.
 */
public class SMSDataParser {
    private ArrayList<SMSData> smsDataList;
    private ArrayList<SMSDataParse> smsDataParseList;

    //Конструктор
    public SMSDataParser(ArrayList<SMSData> smsDataList) {
        this.smsDataList = smsDataList;
    }

    public ArrayList<SMSDataParse> GetSMSDataParse() {
        smsDataParseList = new ArrayList<>();
        smsDataParseList.clear();

        /*//Mock ing
        smsDataList.clear();
        SMSData data = new SMSData();
        data.setBody("Выписка от 27.12 по карте *5825. Начислено процентов: 304.98 руб. Cashback: 598 руб. Баланс на 27.12: 45879.06 руб. www.tinkoff.ru");
        data.setNumber("tinkoff.ru");
        smsDataList.add(data);*/

        if (smsDataList != null) for (SMSData item : smsDataList)
            smsDataParseList.addAll(parse(item));
        return smsDataParseList;

        /*if (smsDataList != null) return null;*/


    }

    private ArrayList<SMSDataParse> parse(SMSData input) {
        String[] tokens = input.getBody().split("\\. ");
        SMSDataParse data = new SMSDataParse();
        SMSDataParse data2 = new SMSDataParse(); //ToDo saw ugly
        ArrayList<SMSDataParse> result = new ArrayList<>();

        /*
        0 = {String@831718550832} "Pokupka"
        1 = {String@831718550928} "Karta *6296"
        2 = {String@831718550960} "Summa 451.00 RUB"
        3 = {String@831718550992} "PYATEROCHKA 3156, IVANOVO"
        4 = {String@831718551024} "17.01.2016 17:19"
        5 = {String@831718551056} "Dostupno 24600.67 RUB"
        6 = {String@831718551088} "www.tinkoff.ru"

        0 = {String@831722598176} "Выписка от 27.12 по карте *5825"
        1 = {String@831722598272} "Начислено процентов: 304.98 руб"
        2 = {String@831722598304} "Cashback: 598 руб"
        3 = {String@831722598336} "Баланс на 27.12: 45879.06 руб"
        4 = {String@831722598368} "www.tinkoff.ru"
        */

        int procPos, coastPos, userPos, placePos, datePos, balancePos;
        data.setProcedure(parseProcedure(tokens[0])); //ToDo if null
        if (data.getProcedure() == enProcedure.Percent) {
            data.setUser(enUser.Maks);                         //User
            data.setLocation("Тинькофф");                      //Location
            for (int i = 0; i < tokens.length; i++) {
                if (isContain(tokens[i], "Начислено")) {
                    data.setCoast(parseCoast(tokens[i]));      //Coast
                } else if (isContain(tokens[i], "Выписка от"))
                    data.setDate(parseDate(tokens[i]));       //Date
                else if (isContain(tokens[i], "Баланс"))
                    data.setBalance(parseBalance(tokens[i]));       //Balance
            }
            result.add(data);
            //ToDo Hardcoded
            data2.setProcedure(enProcedure.Cashback);
            data2.setUser(enUser.Maks);                         //User
            data2.setLocation("Тинькофф");
            data2.setDate(data.getDate());
            data2.setBalance(data.getBalance());
            for (int i = 0; i < tokens.length; i++)
                if (isContain(tokens[i], "Cashback"))
                    data2.setCoast(parseCoast(tokens[i]));      //Coast
            result.add(data2);
        } else if (data.getProcedure() != enProcedure.Unknown){
            for (int i = 0; i < tokens.length; i++) {
                if (isContain(tokens[i], "Karta"))
                    data.setUser(parseUser(tokens[i]));       //User
                else if (isContain(tokens[i], "Summ")) {
                    data.setCoast(parseCoast(tokens[i]));     //Coast
                    data.setLocation(tokens[i + 1]);          //Location
                } else if (isContain(tokens[i], ":")
                        || isContain(tokens[i], "Выписка"))
                    data.setDate(parseDate(tokens[i]));       //Date
                else if (isContain(tokens[i], "Dostupno")
                        || isContain(tokens[i], "Баланс"))
                    data.setBalance(parseBalance(tokens[i]));       //Balance
            }
            result.add(data);
        }

        return result;
    }

    private Calendar parseDate(String s) {
        String[] split = s.split(" ");
        String[] date;
        Calendar result;
        //split[0]= "Выписка";
        if (split[0].contains("Выписка")) {
            date = split[2].split("\\.");
            result = new GregorianCalendar(
                    Calendar.getInstance().get(Calendar.YEAR),
                    Integer.parseInt(date[1]),
                    Integer.parseInt(date[0])
            );
        } else {
            date = split[0].split("\\.");
            result = new GregorianCalendar(
                    Integer.parseInt(date[2]),
                    Integer.parseInt(date[1]),
                    Integer.parseInt(date[0])
            );
        }
        return result;
    }

    private enProcedure parseProcedure (String s){
        Map procMap = new HashMap<String, enProcedure>();

        procMap.put("Pokupka", enProcedure.Purchase);
        procMap.put("Jur. perevod", enProcedure.Purchase);
        procMap.put("Platezh", enProcedure.Purchase);
        procMap.put("Vneshniy perevod", enProcedure.Purchase);

        procMap.put("Snyatie nalichnyh", enProcedure.CashOut);

        procMap.put("Popolnenie", enProcedure.Input);
        procMap.put("Popolnenie", enProcedure.Input);

        procMap.put("Otmena pokupki", enProcedure.Deny);

        procMap.put("Выписка", enProcedure.Percent);

        enProcedure result = (enProcedure) procMap.get(s);

        if (result == null) {
            result = (enProcedure) procMap.get(s.split(" ")[0]);
            if (result == null)
                result = enProcedure.Unknown;
        }
        return result;
    }

    private enUser parseUser(String s) {
        Map procMap = new HashMap<String, enProcedure>();
            procMap.put("*6296", enUser.Maks);
            procMap.put("*5825", enUser.Alina);


        enUser result = (enUser) procMap.get(getElement(s.split(" "), "Karta", 1));
        if (result == null) {
            result = enUser.Unknown;
        }
        return result;
    }

    private Double parseCoast(String s) {
        String usage;
        Double result;
        try {
            usage = getElement(s.split(" "), "руб", -1);
            if (usage == "")
                usage = getElement(s.split(" "), "Sum", 1);
            result = Double.parseDouble(usage);
        } catch (NumberFormatException e) {
            result = 0.0;
        }
        return result;
    }

    private Double parseBalance(String s) {
        String usage;
        Double result;
        try {
            usage = getElement(s.split(" "), "RUB", -1);
            if (usage == "")
                usage = getElement(s.split(" "), "руб", -1);
            if (usage == "")
                usage = getElement(s.split(" "), "Баланс", 3);
            if (usage == "") {
                usage = getElement(s.split(" "), "Dostupno", 1);
            }
            result = Double.parseDouble(usage);
        } catch (NumberFormatException e) {
            result = 0.0;
        }
        return result;
    }

    private String getElement(String[] tokens, String find, int shift) {
        String result = "";
        //Todo Tokens nn
        for (int i = 0; i < tokens.length; i++)
            if (isContain(tokens[i], find))
                result = tokens[i + shift];
        return result;
    }

    private boolean isContain (String st, String subst) {
        return st.toLowerCase().contains(subst.toLowerCase());
    }

}



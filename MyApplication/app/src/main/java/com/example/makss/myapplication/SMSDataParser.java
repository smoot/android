package com.example.makss.myapplication;

import java.util.ArrayList;

/**
 * Created by makss on 22.01.2016.
 */
public class SMSDataParser {
    private ArrayList<SMSData> smsDataList;
    private ArrayList<SMSDataParse> smsDataParseList;
    private Procedure proc = new Procedure();

    //Конструктор
    public SMSDataParser(ArrayList<SMSData> smsDataList) {
        this.smsDataList = smsDataList;
    }

    public ArrayList<SMSDataParse> GetSMSDataParse() {
        smsDataParseList = new ArrayList<>();
        smsDataParseList.clear();

        //Mocking
        smsDataList.clear();
        SMSData data = new SMSData();
        data.setBody("Pokupka. Karta *6296. Summa 451.00 RUB. PYATEROCHKA 3156, IVANOVO. 17.01.2016 17:19. Dostupno 24600.67 RUB. www.tinkoff.ru");
        data.setNumber("tinkoff.ru");
        smsDataList.add(data);
        //End Mocking
        if (smsDataList != null) {
            for (int i = 0; i < smsDataList.size(); i++)
                smsDataParseList.add(i, parse(smsDataList.get(i)));
        }
        return smsDataParseList;

        /*if (smsDataList != null) return null;*/


    }

    private SMSDataParse parse(SMSData input) {
        String phrase = input.getBody();
        String delims = "\\. ";
        String[] tokens = phrase.split(delims);
        SMSDataParse result = new SMSDataParse();

        /*0 = {String@831718550832} "Pokupka"
        1 = {String@831718550928} "Karta *6296"
        2 = {String@831718550960} "Summa 451.00 RUB"
        3 = {String@831718550992} "PYATEROCHKA 3156, IVANOVO"
        4 = {String@831718551024} "17.01.2016 17:19"
        5 = {String@831718551056} "Dostupno 24600.67 RUB"
        6 = {String@831718551088} "www.tinkoff.ru"*/


        result.setProcedure(proc.getProcedure(tokens[0])); //ToDo null
//        result.setUser; //ToDo exec
        result.setPrice(getPrice(tokens));
//        result.setCurrensy  //ToDo exec
//        result.setLocation(tokens);




        return result;
    }

    private float getPrice(String[] st) {
        int pos = 3;
        for (int i=0; i<st.length; i++)
            if (st[i].equals("Summ"))
                pos=i+1; //ToDo Ex + break

        float result = Float.parseFloat(st[pos]);

        return result;
    }



    private boolean isContain (String st, String subst) {
        return st.toLowerCase().contains(subst.toLowerCase());
    }

}



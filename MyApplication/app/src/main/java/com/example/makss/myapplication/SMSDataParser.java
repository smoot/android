package com.example.makss.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by makss on 22.01.2016.
 */
public class SMSDataParser {
    private ArrayList<SMSData> smsDataList;
    private ArrayList<SMSDataParse> smsDataParseList;
    private Procedures proc = new Procedures();

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
        String delims = ". ";
        String[] tokens = phrase.split(delims);
        SMSDataParse result = new SMSDataParse();

        /*0 = {String@831718645112} "Pokupka"
        1 = {String@831718645208} "Kart"
        2 = {String@831718645240} "*6296"
        3 = {String@831718645272} "Summ"
        4 = {String@831718645304} "451.0"
        5 = {String@831718645336} "RUB"
        6 = {String@831718645368} "PYATEROCHK"
        7 = {String@831718645400} "3156"
        8 = {String@831718645432} "IVANOVO"
        9 = {String@831718645464} "17.01.201"
        10 = {String@831718645496} "17:19"
        11 = {String@831718645528} "Dostupn"
        12 = {String@831718645560} "24600.6"
        13 = {String@831718645680} "RUB"
        14 = {String@831718645712} "www.tinkoff.ru"*/

        result.setProcedure(proc.getProcedure(tokens[0]));
        String aa="";




        return result;
    }



    private boolean isContain (String st, String subst) {
        return st.toLowerCase().contains(subst.toLowerCase());
    }

}



package com.example.makss.myapplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by makss on 23.01.2016.
 */
public class Procedures {
    private enum procedure {
        Purchase, Input, CashOut, Code, Cashback, Percent, Info, Deny
    }

    private Map procMap;

    public Procedures() {
        procMap = new HashMap<String, procedure>();
        procMap.put("Pokupka", procedure.Purchase);
        procMap.put("Snyatie nalichnyh", procedure.CashOut);

    }

    public String getProcedure(String st) {
        String result = procMap.get(st).toString();
        return result;
    }

}

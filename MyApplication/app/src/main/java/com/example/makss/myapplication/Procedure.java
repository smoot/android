package com.example.makss.myapplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by makss on 23.01.2016.
 */
public class Procedure {
    private Map procMap;

    public Procedure() {
        procMap = new HashMap<String, enProcedure>();
        procMap.put("Pokupka", enProcedure.Purchase);
        procMap.put("Snyatie nalichnyh", enProcedure.CashOut);

    }

    public enProcedure getProcedure(String st) {
        enProcedure result = (enProcedure) procMap.get(st);
        return result;
    }

}

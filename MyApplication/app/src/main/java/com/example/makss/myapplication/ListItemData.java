package com.example.makss.myapplication;


public class ListItemData {

    private String[] strings;

    public ListItemData(int count){
        strings = new String[count];
    }

    public String getString(int index) {
        return strings[index];
    }

    public void setString(int index, String string) {
        this.strings[index] = string;
    }

}

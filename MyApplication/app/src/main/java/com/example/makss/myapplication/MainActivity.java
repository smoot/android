package com.example.makss.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.R.color;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    ArrayList<SMSData> smsList = new ArrayList<>();
    ListAdapter listAdapter;
    ListView lvMain;
    Button viewInbox, viewSent, viewTinkoff, viewParse;
    SMSDataParser parser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // создаем адаптер


        listAdapter = new ListAdapter(this, smsList);

        // настраиваем список

        lvMain = (ListView) findViewById(R.id.list);

        viewInbox = (Button) findViewById(R.id.viewInbox);
        viewSent = (Button) findViewById(R.id.viewSent);
        viewTinkoff = (Button) findViewById(R.id.viewTinkoff);
        viewParse = (Button) findViewById(R.id.viewParse);


    }


    public void onInboxClick(View view) {
        fillData(getSMSData("inbox", null));
        viewInbox.setBackgroundColor(Color.BLUE);
        viewSent.setBackgroundColor(0xffffffff);
        viewTinkoff.setBackgroundColor(0xffffffff);
    }

    public void onSentClick(View view) {
        fillData(getSMSData("sent", null));
        viewInbox.setBackgroundColor(0xffffffff);
        viewSent.setBackgroundColor(Color.BLUE);
        viewTinkoff.setBackgroundColor(0xffffffff);
    }

    public void onTinkoffClick(View view) {
        fillData(getSMSData("inbox", "address LIKE '%Tinko%'"));
        viewInbox.setBackgroundColor(0xffffffff);
        viewSent.setBackgroundColor(0xffffffff);
        viewTinkoff.setBackgroundColor(Color.BLUE);
    }

    public void onParseClick(View view) {
        parse(getSMSData("inbox", "address LIKE '%Tinko%'"));
        viewInbox.setBackgroundColor(0xffffffff);
        viewSent.setBackgroundColor(0xffffffff);
        viewTinkoff.setBackgroundColor(0xffffffff);
        viewParse.setBackgroundColor(Color.BLUE);
    }

    // генерируем данные для адаптера
    void fillData(Cursor c) {
        smsList.clear();

        TextView quantity = (TextView) findViewById(R.id.quantity);
        if (c != null) {
            quantity.setText(String.valueOf(c.getCount()));
        }

        // Read the sms data and store it in the list
        if (c != null && c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                SMSData sms = new SMSData();
                sms.setBody(c.getString(c.getColumnIndexOrThrow("body")));
                sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")));
                smsList.add(sms);

                c.moveToNext();
            }
        }
        if (c != null) {
            c.close();
        }

        lvMain.setAdapter(listAdapter);
    }

    Cursor getSMSData(String smsfolder, String filter) {
        Uri uri = Uri.parse("content://sms/inbox");

        switch (smsfolder) {
            case "inbox":
                uri = Uri.parse("content://sms/inbox");
                break;
            case "sent":
                uri = Uri.parse("content://sms/sent");
                break;
            case "draft":
                uri = Uri.parse("content://sms/draft");
                break;
            case "outbox":
                uri = Uri.parse("content://sms/outbox");
                break;
        }

        Cursor c = getContentResolver().query(uri, null, filter, null, null);
        return c;
    }

    void parse(Cursor c) {
        parser = new SMSDataParser(c);


    }
}


    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SMSData sms = (SMSData)getListAdapter().getItem(position);

        Toast.makeText(getApplicationContext(), sms.getBody(), Toast.LENGTH_LONG).show();

    }

}*/


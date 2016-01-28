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

import java.util.ArrayList;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {

    ArrayList<SMSData> smsList = new ArrayList<>();
    ListView lvMain;
    Button viewInbox, viewSent, viewTinkoff, viewParse;
    TextView quantity;
    SMSDataParser parser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // создаем адаптер


        // настраиваем список
        lvMain = (ListView) findViewById(R.id.list);

        //Кнопки
        viewInbox = (Button) findViewById(R.id.viewInbox);
        viewSent = (Button) findViewById(R.id.viewSent);
        viewTinkoff = (Button) findViewById(R.id.viewTinkoff);
        viewParse = (Button) findViewById(R.id.viewParse);
        quantity = (TextView) findViewById(R.id.quantity);
        get();

    }

    public void onInboxClick(View view) {
        fillData(getSMSList("inbox", null));
        viewInbox.setBackgroundColor(Color.BLUE);
        viewSent.setBackgroundColor(0xffffffff);
        viewTinkoff.setBackgroundColor(0xffffffff);
    }

    public void onSentClick(View view) {
        fillData(getSMSList("sent", null));
        viewInbox.setBackgroundColor(0xffffffff);
        viewSent.setBackgroundColor(Color.BLUE);
        viewTinkoff.setBackgroundColor(0xffffffff);
    }

    public void onTinkoffClick(View view) {
        fillData(getSMSList("inbox", "address LIKE '%Tinko%'"));
        viewInbox.setBackgroundColor(0xffffffff);
        viewSent.setBackgroundColor(0xffffffff);
        viewTinkoff.setBackgroundColor(Color.BLUE);
    }

    public void onParseClick(View view) {
        fillData(parse(getSMSList("inbox", "address LIKE '%Tinko%'")));
        viewInbox.setBackgroundColor(0xffffffff);
        viewSent.setBackgroundColor(0xffffffff);
        viewTinkoff.setBackgroundColor(0xffffffff);
        viewParse.setBackgroundColor(Color.BLUE);
    }

    // генерируем данные для адаптера
    void fillData(ArrayList<SMSData> l) {
        ListAdapter listAdapter = new ListAdapter(this, l);

        if (l != null) {
            quantity.setText(String.valueOf(l.size()));
        }

        lvMain.setAdapter(listAdapter);
    }

    ArrayList<SMSData> getSMSList(String smsfolder, String filter) {
        // Read the sms data and store it in the list
        ArrayList<SMSData> l = new ArrayList<>();
        Cursor c = getSMSData(smsfolder, filter);
        if (c != null && c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                SMSData sms = new SMSData();
                sms.setBody(c.getString(c.getColumnIndexOrThrow("body")));
                sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")));
                l.add(sms);

                c.moveToNext();
            }
        }
        if (c != null) {
            c.close();
        }


        return l;
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

    ArrayList<SMSData> parse(ArrayList<SMSData> l) {
        parser = new SMSDataParser(l);
        ArrayList<SMSDataParse> list = parser.GetSMSDataParse();
        ArrayList<SMSData> newlist = new ArrayList<>();
        for (SMSDataParse data : list) {
            SMSData sms = new SMSData();

            sms.setBody(data.getProcedure().toString() + " " + data.getLocation().toString() + " " + Double.toString(data.getBalance()));

            sms.setNumber(Double.toString(data.getCoast()) + " " + String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", data.getDate()) + " " + data.getUser().toString());

            newlist.add(sms);
        }

        return newlist;
    }

    void get() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.56.1:3000", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String aa = responseBody.toString();
                String bbb = "";
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

};


    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SMSData sms = (SMSData)getListAdapter().getItem(position);

        Toast.makeText(getApplicationContext(), sms.getBody(), Toast.LENGTH_LONG).show();

    }

}*/


package com.example.makss.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {

    ListView lvMain;
    Button viewInbox, viewSent, viewTinkoff, viewParse;
    TextView quantity;
    SMSDataParser parser;
    httpClient client;
    AsyncHttpResponseHandler handler;
    ListAdapter listAdapter;

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

        client = new httpClient();

        handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(MainActivity.this, "Request success. Status code is " + String.valueOf(statusCode), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Request failure. Status code is " + String.valueOf(statusCode), Toast.LENGTH_SHORT).show();
            }
        };

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "position is " + String.valueOf(position) + ", id is " + String.valueOf(id), Toast.LENGTH_SHORT).show();
                RequestParams params = new RequestParams();
                SMSDataParse data = listAdapter.getSMSDataParseItem(position);
                params.put("procedure", data.getProcedure().toString());
                params.put("coast", Double.toString(data.getCoast()));
                params.put("location", data.getLocation().toString());
                params.put("data", String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", data.getDate()));
                params.put("user", data.getUser().toString());
                params.put("balance", Double.toString(data.getBalance()));
                client.asyncPost("/update", params, handler);
            }
        });

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
        fillDataParse(parse(getSMSList("inbox", "address LIKE '%Tinko%'")));
        viewInbox.setBackgroundColor(0xffffffff);
        viewSent.setBackgroundColor(0xffffffff);
        viewTinkoff.setBackgroundColor(0xffffffff);
        viewParse.setBackgroundColor(Color.BLUE);
    }

    void fillData(ArrayList<SMSData> l) {
        listAdapter = new ListAdapter(this, l);

        if (l != null) {
            quantity.setText(String.valueOf(l.size()));
        }

        lvMain.setAdapter(listAdapter);
    }

    void fillDataParse(ArrayList<SMSDataParse> l) {
        ArrayList<SMSData> newlist = new ArrayList<>();
        for (SMSDataParse data : l) {
            SMSData sms = new SMSData();

            sms.setBody(data.getProcedure().toString() + " " + data.getLocation().toString() + " " + Double.toString(data.getBalance()));

            sms.setNumber(Double.toString(data.getCoast()) + " " + String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", data.getDate()) + " " + data.getUser().toString());

            newlist.add(sms);
        }
        ;
        fillData(newlist);
        listAdapter.setSMSDataParse(l);

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

    ArrayList<SMSDataParse> parse(ArrayList<SMSData> l) {
        parser = new SMSDataParser(l);
        ArrayList<SMSDataParse> list = parser.GetSMSDataParse();
        return list;
    }

}
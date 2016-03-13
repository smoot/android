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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {

    ListView lvMain;
    Button viewBalance, viewTinkoff, viewParse;
    TextView quantity;
    SMSDataParser parser;
    httpClient client;
    AsyncHttpResponseHandler handler, handler1;
    ListAdapter listAdapter;
    Locale locale = Locale.US;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // создаем адаптер


        // настраиваем список
        lvMain = (ListView) findViewById(R.id.list);

        //Кнопки
        viewBalance = (Button) findViewById(R.id.viewBalance);
        viewTinkoff = (Button) findViewById(R.id.viewTinkoff);
        viewParse = (Button) findViewById(R.id.viewParse);
        quantity = (TextView) findViewById(R.id.quantity);

        client = new httpClient();

        handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String aaa = new String(responseBody);
                ;
                Toast.makeText(MainActivity.this, "Request success. Status code is " + String.valueOf(statusCode) +
                        " \n Balance is: " + aaa, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Request failure. Status code is " + String.valueOf(statusCode), Toast.LENGTH_SHORT).show();
            }
        };

        handler1 = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String aaa = new String(responseBody);
                JSONObject jsonResult = null;
                try {
                    jsonResult = new JSONObject(aaa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                ToDo Balance to View!!!
                Toast.makeText(MainActivity.this, "Request success. Status code is " + String.valueOf(statusCode) +
                        " \n Balance is: " + aaa, Toast.LENGTH_SHORT).show();
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
                params.put("date", String.format(locale, "%1$tb %1$td %1$tY", data.getDate()));
                params.put("user", data.getUser().toString());
                params.put("balance", Double.toString(data.getBalance()));
                client.asyncPost("/smsdata", params, handler);
            }
        });

    }


    public void onBalanceClick(View view) {
        getBalanceList();
//        fillData(getSMSList("sent", null)); //FIXME
        viewParse.setBackgroundColor(0xffffffff);
        viewBalance.setBackgroundColor(Color.BLUE);
        viewTinkoff.setBackgroundColor(0xffffffff);
    }

    public void onTinkoffClick(View view) {
        fillData(getSMSList("inbox", "address LIKE '%Tinko%'"));
        viewParse.setBackgroundColor(0xffffffff);
        viewBalance.setBackgroundColor(0xffffffff);
        viewTinkoff.setBackgroundColor(Color.BLUE);
    }

    public void onParseClick(View view) {
        fillDataParse(parse(getSMSList("inbox", "address LIKE '%Tinko%'")));
        viewBalance.setBackgroundColor(0xffffffff);
        viewTinkoff.setBackgroundColor(0xffffffff);
        viewParse.setBackgroundColor(Color.BLUE);
    }

    void fillData(ArrayList<ListItemData> l) {
        listAdapter = new ListAdapter(this, l);

        if (l != null) {
            quantity.setText(String.valueOf(l.size()));
        }

        lvMain.setAdapter(listAdapter);
    }

    void fillDataParse(ArrayList<SMSDataParse> l) {
        ArrayList<ListItemData> newlist = new ArrayList<>();
        for (SMSDataParse data : l) {
            ListItemData sms = new ListItemData(2);

            sms.setString(0, data.getProcedure().toString() + " " + data.getLocation().toString() + " " + Double.toString(data.getBalance()));

            sms.setString(1, Double.toString(data.getCoast()) + " " + String.format("%1$tb %1$td %1$tY", data.getDate()) + " " + data.getUser().toString());

            newlist.add(sms);
        }
        ;
        fillData(newlist);
        listAdapter.setSMSDataParse(l);

    }

   void getBalanceList(){

        client.asyncGet("/smsdata/balance", null, handler1);

    }

    ArrayList<ListItemData> getSMSList(String smsfolder, String filter) {
        // Read the sms data and store it in the list
        ArrayList<ListItemData> l = new ArrayList<>();
        Cursor c = getSMSData(smsfolder, filter);
        if (c != null && c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                ListItemData sms = new ListItemData(2);
                sms.setString(1, c.getString(c.getColumnIndexOrThrow("body")));
                sms.setString(0, c.getString(c.getColumnIndexOrThrow("address")));
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

    ArrayList<SMSDataParse> parse(ArrayList<ListItemData> l) {
        parser = new SMSDataParser(l);
        ArrayList<SMSDataParse> list = parser.GetSMSDataParse();
        return list;
    }

}
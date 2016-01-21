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
    Button viewInbox;
    Button viewSent;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // создаем адаптер


        listAdapter = new ListAdapter(this, smsList);

        // настраиваем список

        lvMain = (ListView) findViewById(R.id.list);

        viewInbox = (Button) findViewById(R.id.viewInbox);
        viewSent = (Button) findViewById(R.id.viewSent);

    }



    public void onInboxClick(View view)
    {
        fillData("inbox");
        viewInbox.setBackgroundColor(Color.BLUE);
        viewSent.setBackgroundColor(0xffffffff);
    }

    public void onSentClick(View view)
    {
        fillData("sent");
        viewInbox.setBackgroundColor(0xffffffff);
        viewSent.setBackgroundColor(Color.BLUE);
    }

    // генерируем данные для адаптера
    void fillData(String smsfolder) {
        smsList.clear();
        Cursor c = getSMSData(smsfolder);

        TextView quantity = (TextView)findViewById(R.id.quantity);
        if (c != null) {
            quantity.setText(String.valueOf(c.getCount()));
        }

        // Read the sms data and store it in the list
        if(c != null && c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
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

    Cursor getSMSData(String smsfolder) {
        Uri uri = Uri.parse("content://sms/inbox");

        switch (smsfolder){
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

        Cursor c= getContentResolver().query(uri, null, null ,null,null);
        return c;
    };

}



    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SMSData sms = (SMSData)getListAdapter().getItem(position);

        Toast.makeText(getApplicationContext(), sms.getBody(), Toast.LENGTH_LONG).show();

    }

}*/


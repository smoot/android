package com.example.makss.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends Activity {

    ArrayList<SMSData> smsList = new ArrayList<>();
    ListAdapter listAdapter;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // создаем адаптер

        fillData();
        listAdapter = new ListAdapter(this, smsList);

        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.list);
        lvMain.setAdapter(listAdapter);
    }

    // генерируем данные для адаптера
    void fillData() {
        Uri uri = Uri.parse("content://sms/sent");
        Cursor c= getContentResolver().query(uri, null, null ,null,null);
        startManagingCursor(c);

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

        /*for (int i = 1; i <= 20; i++) {
            smsList.add(new SMSData("SMSData " + i, i * 1000,
                    R.drawable.ic_launcher, false));
        }*/
    }

    /*// выводим информацию о корзине
    public void showResult(View v) {
        String result = "Товары в корзине:";
        for (SMSData p : listAdapter.getBox()) {
            if (p.box)
                result += "\n" + p.name;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }*/


    /*// Set smsList in the ListAdapter
    setListAdapter(new ListAdapter(this, smsList));*/

}

/*import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    String[] names = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей" };

    *//** Called when the activity is first created. *//*
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // находим список
        ListView lvMain = (ListView) findViewById(R.id.list);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

    }
}*/

/*
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<SMSData> smsList = new ArrayList<SMSData>();

        Uri uri = Uri.parse("content://sms/sent");
        Cursor c= getContentResolver().query(uri, null, null ,null,null);
        startManagingCursor(c);

        TextView quantity = (TextView)findViewById(R.id.quantity);
        quantity.setText(String.valueOf(c.getCount()));

        // Read the sms data and store it in the list
        if(c.moveToFirst()) {
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

        // Set smsList in the ListAdapter
        setListAdapter(new ListAdapter(this, smsList));

     */
/*   // находим список
        ListView lvMain = (ListView) findViewById(R.id.lvMain);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);*//*


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SMSData sms = (SMSData)getListAdapter().getItem(position);

        Toast.makeText(getApplicationContext(), sms.getBody(), Toast.LENGTH_LONG).show();

    }

}
*/

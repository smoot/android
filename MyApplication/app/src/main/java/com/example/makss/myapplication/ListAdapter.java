package com.example.makss.myapplication;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<SMSData> objects;
    private ArrayList<SMSDataParse> parse = new ArrayList<SMSDataParse>();

    ListAdapter(Context context, ArrayList<SMSData> SMSList) {
        ctx = context;
        objects = SMSList;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        SMSData p = getSMSData(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.smsNumberText)).setText(p.getNumber());
        ((TextView) view.findViewById(R.id.smsBodyText)).setText(p.getBody());
//        ((ImageView) view.findViewById(R.id.ivImage)).setImageResource(p.image);

       /* CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
        // присваиваем чекбоксу обработчик
        cbBuy.setOnCheckedChangeListener(myCheckChangList);
        // пишем позицию
        cbBuy.setTag(position);
        // заполняем данными из товаров: в корзине или нет
        cbBuy.setChecked(p.box);*/
        return view;
    }

    // товар по позиции
    SMSData getSMSData(int position) {
        return ((SMSData) getItem(position));
    }

    SMSDataParse getSMSDataParseItem(int position) {
        return (parse.get(position));
    }

    public void setSMSDataParse(ArrayList<SMSDataParse> parse) {
        this.parse = parse;
    }

}
    /*// содержимое корзины
    ArrayList<SMSData> getBox() {
        ArrayList<SMSData> box = new ArrayList<SMSData>();
        for (SMSData p : objects) {
            // если в корзине
            if (p.box)
                box.add(p);
        }
        return box;
    }*/

    /*// обработчик для чекбоксов
    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // меняем данные товара (в корзине или нет)
            getSMSData((Integer) buttonView.getTag()).box = isChecked;
        }
    };*/

/*
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.makss.myapplication.SMSData;

public class ListAdapter extends ArrayAdapter<SMSData> {

    // List context
    private final Context context;
    // List values
    private final List<SMSData> smsList;

    public ListAdapter(Context context, List<SMSData> smsList) {
        super(context, R.layout.activity_main, smsList);
        this.context = context;
        this.smsList = smsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_main, parent, true);

        TextView senderNumber = (TextView) rowView.findViewById(R.id.smsNumberText);
        senderNumber.setText(smsList.get(position).getNumber());

        TextView BodyText = (TextView) rowView.findViewById(R.id.smsBodyText);
        BodyText.setText(smsList.get(position).getBody());

        return rowView;
    }

}
*/

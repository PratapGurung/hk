package com.example.pratapgurung.testnavigation;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/*
    class description: this class is array adapter class of order for earning list
    it's constructor receives activity context and List object .

 */
public class earning_list extends ArrayAdapter<Order> {
    private Activity context;
    private List<Order> list;

    public earning_list(Activity context, List<Order> list){
        super(context, R.layout.order_list_layout, list);

        this.context = context;
        this.list = list;
    }
    /*
        overridden fun getView gets the list view and  display item on the list view
        it receives positional index,view and view group parent and inflate the list view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.earning_list_layout, null, true);

        TextView dateView = (TextView) listViewItem.findViewById(R.id.date);
        TextView addressView = (TextView) listViewItem.findViewById(R.id.address);
        TextView incomeView = (TextView) listViewItem.findViewById(R.id.income);


        Order order = list.get(position);
        dateView.setText(order.getCompleteByDate());
        addressView.setText(order.getAddress());
        //first calculate income
        Double income = Double.parseDouble(order.getRate().toString()) * Double.parseDouble( order.getServiceHour().toString());
        incomeView.setText("$ " + income.toString());
        return listViewItem;
    }
}

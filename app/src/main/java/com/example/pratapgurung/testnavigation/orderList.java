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
    this is arrayadapter of order for oderlist
    this arrayadapter can be use to display orders in listview

 */
public class orderList extends ArrayAdapter<Order> {

    private Activity context;
    private List<Order> list;

    public orderList(Activity context, List<Order> list){
        super(context, R.layout.order_list_layout, list);

        this.context = context;
        this.list = list;
    }

    /*
        overridden function that will display items in listView
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.order_list_layout, null, true);

        TextView header = (TextView) listViewItem.findViewById(R.id.header);
        TextView status = (TextView) listViewItem.findViewById(R.id.status);
        TextView requestDate = (TextView) listViewItem.findViewById(R.id.serviceRequestDate);
        TextView description = (TextView) listViewItem.findViewById(R.id.descriptions);

        Order order = list.get(position);

        header.setText(order.getAddress());
        status.setText(order.getStatus());
        requestDate.setText(order.getRequestDate());
        description.setText(order.getDescription());

        return listViewItem;
    }
}

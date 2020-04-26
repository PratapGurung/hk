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

public class orderList extends ArrayAdapter<Order> {

    private Activity context;
    private List<Order> list;

    public orderList(Activity context, List<Order> list){
        super(context, R.layout.order_list_layout, list);

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.order_list_layout, null, true);

        TextView header = (TextView) listViewItem.findViewById(R.id.header);
        TextView customer = (TextView) listViewItem.findViewById(R.id.customerName);
        TextView requestDate = (TextView) listViewItem.findViewById(R.id.serviceRequestDate);
        TextView description = (TextView) listViewItem.findViewById(R.id.description);

        Order order = list.get(position);

        header.setText(order.getAddress());
        customer.setText(order.getCustId());
        requestDate.setText(order.getRequestDate());
        description.setText(order.getDescription());

        return listViewItem;
    }
}

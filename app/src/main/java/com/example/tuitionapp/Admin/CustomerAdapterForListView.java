package com.example.tuitionapp.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuitionapp.R;

public class CustomerAdapterForListView extends BaseAdapter {

    Context context ;
    String name[] ;
    String email[];


    public CustomerAdapterForListView(Context context, String[] name, String[] email) {
        this.context = context;
        this.name = name;
        this.email = email;
    }

    @Override
    public int getCount() {
        return email.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.activity_tutor_list_view, null);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        nameTextView.setText(name[position]);
        emailTextView.setText(email[position]);
        return convertView;
    }
}

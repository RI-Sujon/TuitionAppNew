package com.example.tuitionapp.guardian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuitionapp.R;

import java.util.ArrayList;

public class CustomAdapterForViewingSearchingTutorProfile extends BaseAdapter {
    private Context context;
    private ArrayList<String> nameList ;
    private ArrayList<String> emailList ;


    public CustomAdapterForViewingSearchingTutorProfile(Context context, ArrayList<String> nameList, ArrayList<String> emailList) {
        this.context = context;
        this.nameList = nameList;
        this.emailList = emailList;
    }

    @Override
    public int getCount() {
        return emailList.size() ;
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
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_custom_adapter_list_view, null);
            holder.name = convertView.findViewById(R.id.nameTextView);
            holder.email = convertView.findViewById(R.id.emailTextView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag() ;
        }

        holder.email.setText(emailList.get(position));
        holder.name.setText(nameList.get(position));
        holder.name.setVisibility(View.VISIBLE);
        holder.email.setVisibility(View.VISIBLE);
        return convertView ;
    }

    class ViewHolder {
        TextView  name ;
        TextView email ;
    }
}

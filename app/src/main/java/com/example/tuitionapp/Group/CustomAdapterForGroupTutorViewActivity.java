package com.example.tuitionapp.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuitionapp.R;

import java.util.ArrayList;

public class CustomAdapterForGroupTutorViewActivity extends BaseAdapter {

    private Context context ;
    private ArrayList<AddTutorInfo> addTutorInfoArrayList ;

    public CustomAdapterForGroupTutorViewActivity(Context context, ArrayList<AddTutorInfo> addTutorInfoArrayList) {
        this.context = context;
        this.addTutorInfoArrayList = addTutorInfoArrayList;
    }

    @Override
    public int getCount() {
        return addTutorInfoArrayList.size();
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
        ViewHolder holder ;
        if(convertView==null){
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_custom_adapter_list_view, null);
            holder.email = convertView.findViewById(R.id.nameTextView) ;

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag() ;
        }

        holder.email.setText(addTutorInfoArrayList.get(position).getTutorEmail());
        holder.email.setVisibility(View.VISIBLE);

        return convertView ;
    }

    class ViewHolder{
        TextView email;
    }
}

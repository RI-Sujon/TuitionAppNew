package com.example.tuitionappv1.guardian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuitionappv1.R;

import java.util.ArrayList;

public class CustomAdapterForViewingSearchingGroup extends BaseAdapter {

    Context context;
    ArrayList<String> groupNameList ;

    public CustomAdapterForViewingSearchingGroup(Context context, ArrayList<String> groupNameList) {
        this.context = context;
        this.groupNameList = groupNameList;
    }

    @Override
    public int getCount() {
        return groupNameList.size() ;
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
            holder.groupName = convertView.findViewById(R.id.nameTextView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag() ;
        }

        holder.groupName.setText(groupNameList.get(position));
        holder.groupName.setVisibility(View.VISIBLE);
        return convertView ;
    }

    class ViewHolder {
        TextView groupName ;
    }

}

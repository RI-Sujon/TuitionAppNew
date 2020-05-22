package com.example.tuitionapp_surji.guardian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.group.GroupInfo;

import java.util.ArrayList;

public class CustomAdapterForGroupListView extends BaseAdapter {

    Context context;
    ArrayList<GroupInfo> groupInfoArrayList ;

    public CustomAdapterForGroupListView(Context context, ArrayList<GroupInfo> groupInfoArrayList) {
        this.context = context;
        this.groupInfoArrayList = groupInfoArrayList;
    }

    @Override
    public int getCount() {
        return groupInfoArrayList.size() ;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_adapter_tutor_list_view, null);
            holder.groupName = convertView.findViewById(R.id.name);
            holder.groupAddress = convertView.findViewById(R.id.institute) ;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag() ;
        }

        holder.groupName.setText(groupInfoArrayList.get(position).getGroupName());
        holder.groupAddress.setText(groupInfoArrayList.get(position).getFullAddress() + ", " + groupInfoArrayList.get(position).getAddress() );
        holder.groupName.setVisibility(View.VISIBLE);
        return convertView ;
    }

    class ViewHolder {
        TextView groupName ;
        TextView groupAddress ;
        ImageView groupProfilePic ;
    }

}

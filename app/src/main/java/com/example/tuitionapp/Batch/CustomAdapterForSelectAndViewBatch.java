package com.example.tuitionapp.Batch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuitionapp.R;

import java.util.ArrayList;

public class CustomAdapterForSelectAndViewBatch extends BaseAdapter {

    private Context context ;
    private ArrayList<String>batchName ;

    public CustomAdapterForSelectAndViewBatch(Context context, ArrayList<String> batchName) {
        this.context = context;
        this.batchName = batchName;
    }

    @Override
    public int getCount() {
        return 0;
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
            holder.batchNameTextView = convertView.findViewById(R.id.nameTextView) ;
        }
        else {
            holder = (ViewHolder) convertView.getTag() ;
        }

        holder.batchNameTextView.setText(batchName.get(position));
        holder.batchNameTextView.setVisibility(View.VISIBLE);

        return convertView ;
    }

    class ViewHolder{
        TextView batchNameTextView ;
    }
}

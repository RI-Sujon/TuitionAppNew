package com.example.tuitionappv2.verified_tutor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuitionappv2.R;

import java.util.ArrayList;

public class CustomAdapterForReportTutor extends BaseAdapter {

    private Context context ;
    private ArrayList<ReportInfo>reportInfoArrayList ;

    public CustomAdapterForReportTutor(Context context, ArrayList<ReportInfo> reportInfoArrayList) {
        this.context = context;
        this.reportInfoArrayList = reportInfoArrayList;
    }

    @Override
    public int getCount() {
        return reportInfoArrayList.size();
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
        if (convertView == null) {
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_custom_adapter_list_view, null);
            holder.guardianMobileNumber = convertView.findViewById(R.id.nameTextView);
            holder.message = convertView.findViewById(R.id.emailTextView);
            holder.guardianMobileNumber.setVisibility(View.VISIBLE);
            holder.message.setVisibility(View.VISIBLE);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag() ;
        }

        holder.guardianMobileNumber.setText(reportInfoArrayList.get(position).getGuardianMobileNumber());
        holder.message.setText(reportInfoArrayList.get(position).getMessage());

        return convertView ;
    }

    class ViewHolder{
        TextView guardianMobileNumber ;
        TextView message ;
    }
}

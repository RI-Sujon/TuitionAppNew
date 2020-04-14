package com.example.tuitionappv1.notice_board;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tuitionappv1.R;

import java.util.ArrayList;

public class CustomAdapterForNoticeBoard extends BaseAdapter {
    private Context context ;
    private ArrayList<NoticeInfo> addNoticeInfoArrayList ;

    public CustomAdapterForNoticeBoard(Context context, ArrayList<NoticeInfo> addNoticeInfoArrayList) {
        this.context = context;
        this.addNoticeInfoArrayList = addNoticeInfoArrayList;
    }

    @Override
    public int getCount() {
        return addNoticeInfoArrayList.size();
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
            holder.layout = convertView.findViewById(R.id.noticeLayout) ;
            holder.notice = convertView.findViewById(R.id.noticePostTextView) ;
            holder.attachment = convertView.findViewById(R.id.noticeAttachmentTextView) ;
            holder.noticeDate = convertView.findViewById(R.id.noticeDate) ;
            holder.noticeTime = convertView.findViewById(R.id.noticeTime) ;

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag() ;
        }

        String post = addNoticeInfoArrayList.get(position).getPost();
        String attachmentName = addNoticeInfoArrayList.get(position).getPdfName() ;
        if(attachmentName.equals("")){
            holder.notice.setText(post);
            holder.notice.setVisibility(View.VISIBLE);
        }
        else {
            holder.attachment.setText(attachmentName);
            holder.attachment.setHeight(50);
            holder.attachment.setBackgroundColor(Color.GRAY);
            holder.attachment.setVisibility(View.VISIBLE);
        }
        holder.noticeTime.setText("TIME: " + addNoticeInfoArrayList.get(position).getNoticeTime());
        holder.noticeDate.setText("DATE: " + addNoticeInfoArrayList.get(position).getNoticeDate());
        holder.layout.setVisibility(View.VISIBLE);

        return convertView ;
    }

    class ViewHolder{
        LinearLayout layout ;
        TextView notice;
        TextView attachment;
        TextView noticeDate ;
        TextView noticeTime ;

    }
}

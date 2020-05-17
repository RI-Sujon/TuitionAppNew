package com.example.tuitionapp_sujon.notice_board;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tuitionapp_sujon.R;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_adapter_group_notice_list_view, null);

            holder.title = convertView.findViewById(R.id.notice_title) ;
            holder.notice = convertView.findViewById(R.id.noticePostTextView) ;
            holder.attachment = convertView.findViewById(R.id.noticeAttachmentTextView) ;
            holder.noticeDate = convertView.findViewById(R.id.noticeDate) ;

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag() ;
        }

        String post = addNoticeInfoArrayList.get(position).getPost();
        String attachmentName = addNoticeInfoArrayList.get(position).getPdfName() ;
        if(attachmentName==null){
            holder.notice.setText(post);
            holder.notice.setVisibility(View.VISIBLE);
        }
        else {
            holder.attachment.setText(attachmentName);
            holder.attachment.setVisibility(View.VISIBLE);
        }

        holder.title.setText(addNoticeInfoArrayList.get(position).getTitle());
        holder.noticeDate.setText(addNoticeInfoArrayList.get(position).getNoticeDate() + ",  " +addNoticeInfoArrayList.get(position).getNoticeTime());

        return convertView ;
    }

    class ViewHolder{
        TextView title ;
        TextView notice;
        TextView attachment;
        TextView noticeDate ;
    }
}
